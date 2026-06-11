package com.example.membership.plan.scheduler;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.membership.plan.entity.MembershipTier;
import com.example.membership.plan.entity.Subscription;
import com.example.membership.plan.entity.SubscriptionStatus;
import com.example.membership.plan.entity.UserMonthlyStats;
import com.example.membership.plan.repository.MembershipTierRepository;
import com.example.membership.plan.repository.SubscriptionRepository;
import com.example.membership.plan.repository.UserMonthlyStatsRepository;
import com.example.membership.plan.strategy.TierEvaluationEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TierEvaluationScheduler {

    private static final String DEFAULT_CRON = "0 0 2 * * *";

    private final SubscriptionRepository subscriptionRepository;
    private final UserMonthlyStatsRepository userMonthlyStatsRepository;
    private final MembershipTierRepository membershipTierRepository;
    private final TierEvaluationEngine tierEvaluationEngine;
    private final Clock clock = Clock.systemUTC();

    @Scheduled(cron = "${membership.tier-evaluation.cron:" + DEFAULT_CRON + "}")
    @Transactional
    public void evaluateDailyTiers() {
        LocalDate monthStartDate = LocalDate.now(clock).withDayOfMonth(1);
        List<MembershipTier> activeTiers = membershipTierRepository.findByActiveTrueOrderByRankOrderAsc();

        if (activeTiers.isEmpty()) {
            log.warn("Skipping tier evaluation because no active membership tiers exist");
            return;
        }

        subscriptionRepository.findByStatus(SubscriptionStatus.ACTIVE)
                .forEach(subscription -> evaluateSubscription(subscription, activeTiers, monthStartDate));
    }

    private void evaluateSubscription(
            Subscription subscription,
            List<MembershipTier> activeTiers,
            LocalDate monthStartDate) {

        userMonthlyStatsRepository
                .findBySubscriptionIdAndMonthStartDate(subscription.getId(), monthStartDate)
                .ifPresentOrElse(
                        stats -> updateTierIfRequired(subscription, stats, activeTiers),
                        () -> log.debug(
                                "Skipping tier evaluation for subscription {} because stats are missing for {}",
                                subscription.getId(),
                                monthStartDate));
    }

    private void updateTierIfRequired(
            Subscription subscription,
            UserMonthlyStats stats,
            List<MembershipTier> activeTiers) {

        MembershipTier evaluatedTier = tierEvaluationEngine.evaluate(stats, activeTiers);

        if (subscription.getTier().getId().equals(evaluatedTier.getId())) {
            return;
        }

        MembershipTier previousTier = subscription.getTier();
        log.info(
                "Updating subscription {} tier from {} to {}",
                subscription.getId(),
                previousTier.getName(),
                evaluatedTier.getName());
        subscription.setTier(evaluatedTier);
        subscriptionRepository.save(subscription);
    }
}
