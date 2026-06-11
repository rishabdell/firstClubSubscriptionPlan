package com.example.membership.plan.strategy;

import com.example.membership.plan.entity.MembershipTier;
import com.example.membership.plan.entity.UserMonthlyStats;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(10)
public class OrderCountStrategy implements TierEvaluationStrategy {

    @Override
    public Optional<MembershipTier> evaluate(UserMonthlyStats stats, List<MembershipTier> availableTiers) {
        return availableTiers.stream()
                .filter(MembershipTier::isActive)
                .filter(tier -> stats.getOrderCount() >= tier.getMinMonthlyOrders())
                .max(Comparator.comparingInt(MembershipTier::getRankOrder));
    }
}
