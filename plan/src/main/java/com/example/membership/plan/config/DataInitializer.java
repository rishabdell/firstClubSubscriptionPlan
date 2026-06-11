package com.example.membership.plan.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.example.membership.plan.entity.MembershipBenefit;
import com.example.membership.plan.entity.MembershipPlan;
import com.example.membership.plan.entity.MembershipTier;
import com.example.membership.plan.entity.PlanInterval;
import com.example.membership.plan.entity.TierName;
import com.example.membership.plan.repository.MembershipBenefitRepository;
import com.example.membership.plan.repository.MembershipPlanRepository;
import com.example.membership.plan.repository.MembershipTierRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final MembershipPlanRepository planRepository;
    private final MembershipTierRepository tierRepository;
    private final MembershipBenefitRepository benefitRepository;

        @Bean
        CommandLineRunner seedMembershipRunner() {
                return args -> seedMembershipCatalog();
        }

    @Transactional
        void seedMembershipCatalog() {
        MembershipPlan monthly = upsertPlan(PlanInterval.MONTHLY, new BigDecimal("499.00"));
        MembershipPlan quarterly = upsertPlan(PlanInterval.QUARTERLY, new BigDecimal("1299.00"));
        MembershipPlan yearly = upsertPlan(PlanInterval.YEARLY, new BigDecimal("4499.00"));

        MembershipBenefit freeDelivery = upsertBenefit(
                "FREE_DELIVERY",
                "Free Delivery",
                "Free delivery on eligible orders");
        MembershipBenefit prioritySupport = upsertBenefit(
                "PRIORITY_SUPPORT",
                "Priority Support",
                "Faster support for membership issues");
        MembershipBenefit exclusiveDeals = upsertBenefit(
                "EXCLUSIVE_DEALS",
                "Exclusive Deals",
                "Access to member-only offers");

                upsertTier(TierName.SILVER, 1, 0, new BigDecimal("0.00"), List.of(freeDelivery));
                upsertTier(TierName.GOLD, 2, 10, new BigDecimal("2500.00"), List.of(freeDelivery, prioritySupport));
                upsertTier(TierName.PLATINUM, 3, 25, new BigDecimal("7500.00"), List.of(freeDelivery, prioritySupport, exclusiveDeals));
    }

    private MembershipPlan upsertPlan(PlanInterval interval, BigDecimal price) {
        MembershipPlan plan = planRepository.findByInterval(interval)
                .orElseGet(MembershipPlan::new);
        plan.setInterval(interval);
        plan.setPrice(price);
        plan.setActive(true);
        return planRepository.save(plan);
    }

    private MembershipBenefit upsertBenefit(String code, String name, String description) {
        MembershipBenefit benefit = benefitRepository.findByCode(code)
                .orElseGet(MembershipBenefit::new);
        benefit.setCode(code);
        benefit.setName(name);
        benefit.setDescription(description);
        benefit.setActive(true);
        return benefitRepository.save(benefit);
    }

    private MembershipTier upsertTier(
            TierName name,
            int rankOrder,
            int minMonthlyOrders,
            BigDecimal minMonthlySpend,
            List<MembershipBenefit> benefits) {

        MembershipTier tier = tierRepository.findByName(name)
                .orElseGet(MembershipTier::new);
        tier.setName(name);
        tier.setRankOrder(rankOrder);
        tier.setMinMonthlyOrders(minMonthlyOrders);
        tier.setMinMonthlySpend(minMonthlySpend);
        tier.setActive(true);
        tier.getBenefits().clear();
        tier.getBenefits().addAll(benefits);
        return tierRepository.save(tier);
    }
}
