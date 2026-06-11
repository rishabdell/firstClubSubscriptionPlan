package com.example.membership.plan.dto;

import com.example.membership.plan.entity.PlanInterval;
import com.example.membership.plan.entity.TierName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request to create a membership subscription")
public record SubscribeRequest(
        @Schema(description = "Identifier for the subscribing user")
        @NotNull @Positive Long userId,
        @Schema(description = "Requested plan interval")
        @NotNull PlanInterval planInterval,
        @Schema(description = "Requested membership tier")
        @NotNull TierName tierName) {
}
