package com.example.membership.plan.dto;

import com.example.membership.plan.entity.MembershipBenefit;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Membership benefit")
public record BenefitResponse(
        @Schema(description = "Benefit identifier")
        Long id,
        @Schema(description = "Benefit code")
        String code,
        @Schema(description = "Benefit display name")
        String name,
        @Schema(description = "Benefit details")
        String description) {

    public static BenefitResponse from(MembershipBenefit benefit) {
        return new BenefitResponse(
                benefit.getId(),
                benefit.getCode(),
                benefit.getName(),
                benefit.getDescription());
    }
}
