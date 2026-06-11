package com.example.membership.plan.strategy;

import com.example.membership.plan.entity.MembershipTier;
import com.example.membership.plan.entity.Subscription;
import com.example.membership.plan.entity.UserMonthlyStats;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(30)
public class CohortStrategy implements TierEvaluationStrategy {

    @Override
    public Optional<MembershipTier> evaluate(UserMonthlyStats stats, List<MembershipTier> availableTiers) {
        return Optional.ofNullable(stats.getSubscription())
                .map(Subscription::getTier)
                .filter(MembershipTier::isActive)
                .or(() -> findLowestActiveTier(availableTiers));
    }

    private Optional<MembershipTier> findLowestActiveTier(List<MembershipTier> availableTiers) {
        return availableTiers.stream()
                .filter(MembershipTier::isActive)
                .min(Comparator.comparingInt(MembershipTier::getRankOrder));
    }
}
