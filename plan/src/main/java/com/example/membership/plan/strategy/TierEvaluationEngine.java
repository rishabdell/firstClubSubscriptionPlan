package com.example.membership.plan.strategy;

import com.example.membership.plan.entity.MembershipTier;
import com.example.membership.plan.entity.UserMonthlyStats;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TierEvaluationEngine {

    private final List<TierEvaluationStrategy> strategies;

    public MembershipTier evaluate(UserMonthlyStats stats, List<MembershipTier> availableTiers) {
        return strategies.stream()
                .map(strategy -> strategy.evaluate(stats, availableTiers))
                .flatMap(Optional::stream)
                .max(Comparator.comparingInt(MembershipTier::getRankOrder))
                .orElseThrow(() -> new IllegalStateException("No eligible membership tier found"));
    }
}
