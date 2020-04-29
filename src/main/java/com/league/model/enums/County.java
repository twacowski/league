package com.league.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum County {
    MYSZKOW(Voivodeship.SL,"Myszkowski"),
    WODZISLAWSKI(Voivodeship.SL,"Wodzisławski"),
    WARSZAWA(Voivodeship.MZ, "Warszawa"),
    PILA(Voivodeship.KP, "Piła");

    private Voivodeship voivodeship;
    private String displayValue;

    private County(Voivodeship voivodeship, String displayValue) {
        this.voivodeship = voivodeship;
        this.displayValue = displayValue;
    }

    public Voivodeship getVoivodeship() {
        return voivodeship;
    }

    public static List<County> getCounties(Voivodeship voivodeship) {
        List<County> counties = new ArrayList<>();
        for (County county : County.values()){
            if (county.getVoivodeship().equals(voivodeship)) {
                counties.add(county);
            }
        }
        return counties;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
