package com.example.membership.plan.strategy;

import com.example.membership.plan.entity.MembershipTier;
import com.example.membership.plan.entity.UserMonthlyStats;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(20)
public class OrderValueStrategy implements TierEvaluationStrategy {

    @Override
    public Optional<MembershipTier> evaluate(UserMonthlyStats stats, List<MembershipTier> availableTiers) {
        BigDecimal totalSpend = Optional.ofNullable(stats.getTotalSpend()).orElse(BigDecimal.ZERO);

        return availableTiers.stream()
                .filter(MembershipTier::isActive)
                .filter(tier -> totalSpend.compareTo(tier.getMinMonthlySpend()) >= 0)
                .max(Comparator.comparingInt(MembershipTier::getRankOrder));
    }
}
