package com.example.membership.plan.dto;

import java.math.BigDecimal;
import java.util.List;

import com.example.membership.plan.entity.MembershipTier;
import com.example.membership.plan.entity.TierName;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Membership tier and its benefits")
public record TierResponse(
        @Schema(description = "Tier identifier")
        Long id,
        @Schema(description = "Tier name")
        TierName name,
        @Schema(description = "Tier rank for promotion ordering")
        int rankOrder,
        @Schema(description = "Minimum monthly order count required")
        int minMonthlyOrders,
        @Schema(description = "Minimum monthly spend required")
        BigDecimal minMonthlySpend,
        List<BenefitResponse> benefits) {

    public static TierResponse from(MembershipTier tier) {
        return new TierResponse(
                tier.getId(),
                tier.getName(),
                tier.getRankOrder(),
                tier.getMinMonthlyOrders(),
                tier.getMinMonthlySpend(),
                tier.getBenefits().stream()
                        .filter(benefit -> benefit.isActive())
                        .map(BenefitResponse::from)
                        .toList());
    }
}
