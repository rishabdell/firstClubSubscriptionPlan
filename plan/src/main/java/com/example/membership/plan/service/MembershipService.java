package com.example.membership.plan.service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.membership.plan.entity.MembershipBenefit;
import com.example.membership.plan.entity.MembershipPlan;
import com.example.membership.plan.entity.MembershipTier;
import com.example.membership.plan.entity.PlanInterval;
import com.example.membership.plan.entity.Subscription;
import com.example.membership.plan.entity.SubscriptionStatus;
import com.example.membership.plan.entity.TierName;
import com.example.membership.plan.entity.UserMonthlyStats;
import com.example.membership.plan.exception.BusinessRuleException;
import com.example.membership.plan.exception.ConflictException;
import com.example.membership.plan.exception.ResourceNotFoundException;
import com.example.membership.plan.repository.MembershipBenefitRepository;
import com.example.membership.plan.repository.MembershipPlanRepository;
import com.example.membership.plan.repository.MembershipTierRepository;
import com.example.membership.plan.repository.SubscriptionRepository;
import com.example.membership.plan.repository.UserMonthlyStatsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipPlanRepository planRepository;
    private final MembershipTierRepository tierRepository;
    private final MembershipBenefitRepository benefitRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserMonthlyStatsRepository userMonthlyStatsRepository;
    private final Clock clock = Clock.systemUTC();

    /**
     * Business service for managing membership plans and subscriptions.
     * Methods use transactions where appropriate and throw well-defined exceptions
     * for missing resources or business rule violations.
     */

    @Transactional(readOnly = true)
    public List<MembershipPlan> getAllPlans() {
        return planRepository.findByActiveTrue();
    }

    @Transactional(readOnly = true)
    public List<MembershipTier> getAllTiers() {
        return tierRepository.findByActiveTrueOrderByRankOrderAsc();
    }

    @Transactional(readOnly = true)
    public List<MembershipBenefit> getAllBenefits() {
        return benefitRepository.findByActiveTrueOrderByNameAsc();
    }

    @Transactional
    public Subscription subscribe(Long userId, PlanInterval planInterval, TierName tierName) {
        subscriptionRepository.findActiveByUserId(userId)
                .ifPresent(subscription -> {
                    throw new ConflictException("User already has an active subscription");
                });

        MembershipPlan plan = planRepository.findByIntervalAndActiveTrue(planInterval)
                .orElseThrow(() -> new ResourceNotFoundException("Active membership plan not found"));
        MembershipTier tier = tierRepository.findByNameAndActiveTrue(tierName)
                .orElseThrow(() -> new ResourceNotFoundException("Active membership tier not found"));

        LocalDate startDate = LocalDate.now(clock);
        Subscription subscription = Subscription.builder()
                .userId(userId)
                .plan(plan)
                .tier(tier)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(startDate)
                .endDate(startDate.plusMonths(plan.getInterval().getDurationInMonths()))
                .build();

        Subscription saved = subscriptionRepository.save(subscription);
        initializeAssociations(saved);
        return saved;
    }

    @Transactional
    public Subscription cancelSubscription(Long userId) {
        Subscription subscription = getActiveSubscription(userId);
        subscription.setStatus(SubscriptionStatus.CANCELLED);
        subscription.setCancelledAt(Instant.now(clock));
        Subscription saved = subscriptionRepository.save(subscription);
        initializeAssociations(saved);
        return saved;
    }

    @Transactional
    public Subscription upgradeTier(Long userId) {
        Subscription subscription = getActiveSubscription(userId);
        MembershipTier nextTier = findNextTier(subscription.getTier());
        subscription.setTier(nextTier);
        Subscription saved = subscriptionRepository.save(subscription);
        initializeAssociations(saved);
        return saved;
    }

    @Transactional
    public Subscription downgradeTier(Long userId) {
        Subscription subscription = getActiveSubscription(userId);
        MembershipTier downgradedTier = findPreviousTier(subscription.getTier());
        subscription.setTier(downgradedTier);
        Subscription saved = subscriptionRepository.save(subscription);
        initializeAssociations(saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public Subscription getMembership(Long userId) {
        Subscription subscription = getActiveSubscription(userId);
        initializeAssociations(subscription);
        return subscription;
    }

    private void initializeAssociations(Subscription subscription) {
        if (subscription == null) return;
        // access lazy associations inside transaction so DTO mapping outside transaction succeeds
        if (subscription.getPlan() != null) {
            subscription.getPlan().getInterval();
        }
        if (subscription.getTier() != null) {
            subscription.getTier().getName();
        }
    }

    @Transactional
    public UserMonthlyStats trackMembership(Long userId, LocalDate monthStartDate, int orderCount, BigDecimal totalSpend) {
        if (monthStartDate.getDayOfMonth() != 1) {
            throw new BusinessRuleException("monthStartDate must be the first day of the month");
        }

        Subscription subscription = getActiveSubscription(userId);
        UserMonthlyStats stats = userMonthlyStatsRepository
                .findBySubscriptionIdAndMonthStartDate(subscription.getId(), monthStartDate)
                .orElseGet(() -> UserMonthlyStats.builder()
                        .userId(userId)
                        .subscription(subscription)
                        .monthStartDate(monthStartDate)
                        .build());

        stats.setOrderCount(orderCount);
        stats.setTotalSpend(totalSpend);
        return userMonthlyStatsRepository.save(stats);
    }

    @Transactional(readOnly = true)
    public List<UserMonthlyStats> getMembershipTracking(Long userId) {
        Subscription subscription = getActiveSubscription(userId);
        return userMonthlyStatsRepository.findBySubscriptionIdOrderByMonthStartDateDesc(subscription.getId());
    }

    private Subscription getActiveSubscription(Long userId) {
        return subscriptionRepository.findActiveByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Active subscription not found"));
    }

    private MembershipTier findNextTier(MembershipTier currentTier) {
        return tierRepository.findByActiveTrueOrderByRankOrderAsc().stream()
                .filter(tier -> tier.getRankOrder() > currentTier.getRankOrder())
                .min(Comparator.comparingInt(MembershipTier::getRankOrder))
                .orElseThrow(() -> new BusinessRuleException("Subscription is already on the highest tier"));
    }

    private MembershipTier findPreviousTier(MembershipTier currentTier) {
        return tierRepository.findByActiveTrueOrderByRankOrderAsc().stream()
                .filter(tier -> tier.getRankOrder() < currentTier.getRankOrder())
                .max(Comparator.comparingInt(MembershipTier::getRankOrder))
                .orElseThrow(() -> new BusinessRuleException("Subscription is already on the lowest tier"));
    }
}
