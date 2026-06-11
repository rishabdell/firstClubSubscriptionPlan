package com.example.membership.plan.dto;

import java.math.BigDecimal;

import com.example.membership.plan.entity.MembershipPlan;
import com.example.membership.plan.entity.PlanInterval;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Membership plan available for subscription")
public record PlanResponse(
        @Schema(description = "Plan identifier")
        Long id,
        @Schema(description = "Billing interval for the plan")
        PlanInterval interval,
        @Schema(description = "Number of months included in the plan")
        int durationInMonths,
        @Schema(description = "Plan price")
        BigDecimal price) {

    public static PlanResponse from(MembershipPlan plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getInterval(),
                plan.getInterval().getDurationInMonths(),
                plan.getPrice());
    }
}
