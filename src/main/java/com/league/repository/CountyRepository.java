package com.league.repository;

import com.league.model.County;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountyRepository extends JpaRepository<County, Integer> {
    List<County> findByOrderByName();
    List<County> findAllByVoivodeshipId(int id);
}
