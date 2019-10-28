package com.league.repository;

import com.league.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TeamRepository extends PagingAndSortingRepository<Team, Integer> {
    Page<Team> findAllByUserName(Pageable pageable, String username);
    List<Team> getTeamsByUserName(String username);
    List<Team> getTeamsByLeagueId(int id);

}
