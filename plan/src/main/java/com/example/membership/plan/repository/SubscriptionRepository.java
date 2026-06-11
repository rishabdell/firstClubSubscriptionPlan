package com.example.membership.plan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.membership.plan.entity.Subscription;
import com.example.membership.plan.entity.SubscriptionStatus;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    default Optional<Subscription> findActiveByUserId(Long userId) {
        return findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);
    }

    @EntityGraph(attributePaths = {"plan", "tier"})
    Optional<Subscription> findByUserIdAndStatus(Long userId, SubscriptionStatus status);

    List<Subscription> findByStatus(SubscriptionStatus status);

    List<Subscription> findByUserId(Long userId);
}
