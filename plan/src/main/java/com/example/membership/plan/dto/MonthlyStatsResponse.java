package com.example.membership.plan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.membership.plan.entity.UserMonthlyStats;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Stored monthly membership activity")
public record MonthlyStatsResponse(
        @Schema(description = "Record identifier")
        Long id,
        @Schema(description = "User identifier")
        Long userId,
        @Schema(description = "Month covered by this activity")
        LocalDate monthStartDate,
        @Schema(description = "Number of verified orders")
        int orderCount,
        @Schema(description = "Total spend for the month")
        BigDecimal totalSpend) {

    public static MonthlyStatsResponse from(UserMonthlyStats stats) {
        return new MonthlyStatsResponse(
                stats.getId(),
                stats.getUserId(),
                stats.getMonthStartDate(),
                stats.getOrderCount(),
                stats.getTotalSpend());
    }
}
