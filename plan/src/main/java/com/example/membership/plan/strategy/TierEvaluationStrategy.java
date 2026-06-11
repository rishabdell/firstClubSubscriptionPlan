package com.example.membership.plan.strategy;

import com.example.membership.plan.entity.MembershipTier;
import com.example.membership.plan.entity.UserMonthlyStats;
import java.util.List;
import java.util.Optional;

public interface TierEvaluationStrategy {

    Optional<MembershipTier> evaluate(UserMonthlyStats stats, List<MembershipTier> availableTiers);
}
