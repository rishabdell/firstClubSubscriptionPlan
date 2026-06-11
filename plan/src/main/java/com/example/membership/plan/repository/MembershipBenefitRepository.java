package com.example.membership.plan.repository;

import com.example.membership.plan.entity.MembershipBenefit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipBenefitRepository extends JpaRepository<MembershipBenefit, Long> {

    Optional<MembershipBenefit> findByCode(String code);

    List<MembershipBenefit> findByActiveTrueOrderByNameAsc();
}
