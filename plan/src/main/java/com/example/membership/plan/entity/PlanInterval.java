package com.example.membership.plan.entity;

public enum PlanInterval {
    MONTHLY(1),
    QUARTERLY(3),
    YEARLY(12);

    private final int durationInMonths;

    PlanInterval(int durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }
}
