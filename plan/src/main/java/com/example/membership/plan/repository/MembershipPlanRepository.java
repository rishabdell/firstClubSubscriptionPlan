package com.example.membership.plan.repository;

import com.example.membership.plan.entity.MembershipPlan;
import com.example.membership.plan.entity.PlanInterval;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {

    List<MembershipPlan> findByActiveTrue();

    Optional<MembershipPlan> findByInterval(PlanInterval interval);

    Optional<MembershipPlan> findByIntervalAndActiveTrue(PlanInterval interval);
}
