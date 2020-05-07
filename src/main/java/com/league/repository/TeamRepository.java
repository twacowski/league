package com.league.repository;

import com.league.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends PagingAndSortingRepository<Team, Integer> {
    Page<Team> findAllByUserName(Pageable pageable, String username);
    List<Team> getTeamsByUserName(String username);

    @Query("SELECT t FROM Team t WHERE t.name LIKE %:phrase%")
    List<Team> findTeamsByPhrase(@Param("phrase") String phrase);
}
