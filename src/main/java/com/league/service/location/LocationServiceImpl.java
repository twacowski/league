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
    public Map<Integer, String> getCountiesFromVoivodeship(Voivodeship voivodeship) {
        if(voivodeship == null) {
            List<County> counties = countyRepository.findByOrderByName();
            Map<Integer, String> countyValues = new HashMap<>();
            for(County county : counties){
                countyValues.put(county.getId(), county.getName());
            }
            return countyValues;
        }
        List<County> counties = countyRepository.findAllByVoivodeshipId(voivodeship.getId());
        Map<Integer, String> countyValues = new HashMap<>();
        for(County county : counties){
            countyValues.put(county.getId(), county.getName());
        }

        return countyValues;
    }

    @Override
    public List<County> getListOfCountiesFromVoivodeship(Voivodeship voivodeship) {
        if(voivodeship == null) {
            return countyRepository.findByOrderByName();
        }
        return countyRepository.findAllByVoivodeshipId(voivodeship.getId());
    }
}
