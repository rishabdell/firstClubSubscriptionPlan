package com.example.membership.plan.dto;

import java.time.Instant;
import java.time.LocalDate;

import com.example.membership.plan.entity.PlanInterval;
import com.example.membership.plan.entity.Subscription;
import com.example.membership.plan.entity.SubscriptionStatus;
import com.example.membership.plan.entity.TierName;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Membership subscription details")
public record SubscriptionResponse(
        @Schema(description = "Subscription identifier")
        Long id,
        @Schema(description = "Subscribed user identifier")
        Long userId,
        @Schema(description = "Subscription status")
        SubscriptionStatus status,
        @Schema(description = "Plan interval associated with the subscription")
        PlanInterval planInterval,
        @Schema(description = "Current membership tier")
        TierName tierName,
        @Schema(description = "Subscription start date")
        LocalDate startDate,
        @Schema(description = "Subscription end date")
        LocalDate endDate,
        @Schema(description = "Cancellation timestamp if the subscription was cancelled", nullable = true)
        Instant cancelledAt) {

    public static SubscriptionResponse from(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getUserId(),
                subscription.getStatus(),
                subscription.getPlan().getInterval(),
                subscription.getTier().getName(),
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.getCancelledAt());
    }
}
