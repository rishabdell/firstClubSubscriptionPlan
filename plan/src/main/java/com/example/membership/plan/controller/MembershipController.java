package com.example.membership.plan.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.membership.plan.dto.BenefitResponse;
import com.example.membership.plan.dto.MonthlyStatsRequest;
import com.example.membership.plan.dto.MonthlyStatsResponse;
import com.example.membership.plan.dto.PlanResponse;
import com.example.membership.plan.dto.SubscribeRequest;
import com.example.membership.plan.dto.SubscriptionResponse;
import com.example.membership.plan.dto.TierResponse;
import com.example.membership.plan.service.MembershipService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Membership", description = "Membership plan and subscription APIs")
public class MembershipController {

    private final MembershipService membershipService;

    /**
     * Controller exposing membership plan and subscription operations.
     * Keep methods small and return DTOs suitable for API responses.
     */

    @Operation(summary = "Get active membership plans", description = "Returns all active membership plans available for subscription.")
    @GetMapping("/plans")
    public List<PlanResponse> getPlans() {
        return membershipService.getAllPlans().stream()
                .map(PlanResponse::from)
                .toList();
    }

    @Operation(summary = "Get active membership tiers", description = "Returns all active membership tiers and their benefits.")
    @GetMapping("/tiers")
    public List<TierResponse> getTiers() {
        return membershipService.getAllTiers().stream()
                .map(TierResponse::from)
                .toList();
    }

    @Operation(summary = "Get active membership benefits", description = "Returns all active benefits configured for membership tiers.")
    @GetMapping("/benefits")
    public List<BenefitResponse> getBenefits() {
        return membershipService.getAllBenefits().stream()
                .map(BenefitResponse::from)
                .toList();
    }

    @Operation(summary = "Create subscription", description = "Creates an active subscription for a user with the requested plan and tier.")
    @PostMapping("/subscriptions")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponse subscribe(
            @Valid @org.springframework.web.bind.annotation.RequestBody SubscribeRequest request) {
        return SubscriptionResponse.from(membershipService.subscribe(
                request.userId(),
                request.planInterval(),
                request.tierName()));
    }

    @Operation(summary = "Upgrade subscription tier", description = "Moves the user's active subscription to the next higher active tier.")
    @PutMapping("/subscriptions/{userId}/upgrade")
    public SubscriptionResponse upgrade(@PathVariable @Positive Long userId) {
        return SubscriptionResponse.from(membershipService.upgradeTier(userId));
    }

    @Operation(summary = "Downgrade subscription tier", description = "Moves the user's active subscription to the previous lower active tier.")
    @PutMapping("/subscriptions/{userId}/downgrade")
    public SubscriptionResponse downgrade(@PathVariable @Positive Long userId) {
        return SubscriptionResponse.from(membershipService.downgradeTier(userId));
    }

    @Operation(summary = "Cancel subscription", description = "Cancels the active subscription for the given user.")
    @DeleteMapping("/subscriptions/{userId}")
    public SubscriptionResponse cancel(@PathVariable @Positive Long userId) {
        return SubscriptionResponse.from(membershipService.cancelSubscription(userId));
    }

    @Operation(summary = "Get current membership", description = "Returns the user's current active membership subscription.")
    @GetMapping("/subscriptions/{userId}")
    public SubscriptionResponse getMembership(@PathVariable @Positive Long userId) {
        return SubscriptionResponse.from(membershipService.getMembership(userId));
    }

    @Operation(summary = "Upsert monthly membership tracking", description = "Stores monthly order count and spend used by the tier evaluation scheduler.")
    @PutMapping("/subscriptions/{userId}/tracking")
    public MonthlyStatsResponse trackMembership(
            @PathVariable @Positive Long userId,
            @Valid @org.springframework.web.bind.annotation.RequestBody MonthlyStatsRequest request) {

        return MonthlyStatsResponse.from(membershipService.trackMembership(
                userId,
                request.monthStartDate(),
                request.orderCount(),
                request.totalSpend()));
    }

    @Operation(summary = "Get membership tracking", description = "Returns monthly tracking records for the user's active subscription.")
    @GetMapping("/subscriptions/{userId}/tracking")
    public List<MonthlyStatsResponse> getMembershipTracking(@PathVariable @Positive Long userId) {
        return membershipService.getMembershipTracking(userId).stream()
                .map(MonthlyStatsResponse::from)
                .toList();
    }
}
