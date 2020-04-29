package com.league.model.enums;

public enum CompetitionType {
    LEAGUE("League"),
    CUP("Cup"),
    MIX("Group stage + playoffs");

    private final String displayValue;

    private CompetitionType(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
