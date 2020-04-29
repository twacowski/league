package com.league.model.enums;

public enum Voivodeship {
    DS("Dolnośląskie"),
    KP("Kujawsko-pomorskie"),
    LU("Lubelskie"),
    LB("Lubuskie"),
    LD("Łódzkie"),
    MP("Małopolskie"),
    MZ("Mazowieckie"),
    OP("Opolskie"),
    PK("Podkarpackie"),
    PL("Podlaskie"),
    PO("Pomorskie"),
    SL("Śląskie"),
    SW("Świętokrzyskie"),
    WM("Warmińsko-mazurskie"),
    WP("Wielkopolskie"),
    ZP("Zachodniopomorskie");

    private final String displayValue;

    private Voivodeship(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
