package com.league.service.location;

import com.league.model.County;
import com.league.model.Voivodeship;
import com.league.repository.CountyRepository;
import com.league.repository.VoivodeshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    CountyRepository countyRepository;

    @Autowired
    VoivodeshipRepository voivodeshipRepository;

    @Override
    public List<County> getListOfCounties() {
        return countyRepository.findByOrderByName();
    }

    @Override
    public List<Voivodeship> getListOfVoivodeships() {
        return voivodeshipRepository.findAll();
    }

    @Override
    public Map<County, String> getCountiesFromVoivodeship(Voivodeship voivodeship) {
        List<County> counties = countyRepository.findAllByVoivodeshipId(voivodeship.getId());
        System.out.println(counties.size());
        Map<County, String> countyValues = new HashMap<>();
        for(County county : counties){
            countyValues.put(county, county.getName());
        }

        return countyValues;
    }
}
