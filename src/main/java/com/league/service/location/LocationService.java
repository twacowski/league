package com.league.service.location;

import com.league.model.County;
import com.league.model.Voivodeship;

import java.util.List;
import java.util.Map;

public interface LocationService {
    List<County> getListOfCounties();
    List<Voivodeship> getListOfVoivodeships();
    Map<County, String> getCountiesFromVoivodeship(Voivodeship voivodeship);
}
