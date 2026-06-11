package com.example.membership.plan.repository;

import com.example.membership.plan.entity.UserMonthlyStats;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMonthlyStatsRepository extends JpaRepository<UserMonthlyStats, Long> {

    Optional<UserMonthlyStats> findByUserIdAndMonthStartDate(Long userId, LocalDate monthStartDate);

    Optional<UserMonthlyStats> findBySubscriptionIdAndMonthStartDate(Long subscriptionId, LocalDate monthStartDate);

    List<UserMonthlyStats> findByUserIdOrderByMonthStartDateDesc(Long userId);

    List<UserMonthlyStats> findBySubscriptionIdOrderByMonthStartDateDesc(Long subscriptionId);
}
