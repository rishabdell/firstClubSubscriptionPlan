package com.example.membership.plan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Monthly membership activity used for tier evaluation")
public record MonthlyStatsRequest(
        @Schema(description = "First day of the tracking month")
        @NotNull LocalDate monthStartDate,
        @Schema(description = "Number of orders in the month")
        @Min(0) int orderCount,
        @Schema(description = "Total spending for the month")
        @NotNull @DecimalMin("0.00") BigDecimal totalSpend) {
}
