package com.example.membership.plan.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.membership.plan.entity.MembershipTier;
import com.example.membership.plan.entity.TierName;

public interface MembershipTierRepository extends JpaRepository<MembershipTier, Long> {

    Optional<MembershipTier> findByName(TierName name);

    Optional<MembershipTier> findByNameAndActiveTrue(TierName name);

    @EntityGraph(attributePaths = "benefits")
    List<MembershipTier> findByActiveTrueOrderByRankOrderAsc();
}
