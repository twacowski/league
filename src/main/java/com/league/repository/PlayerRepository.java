package com.league.repository;

import com.league.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PlayerRepository extends PagingAndSortingRepository<Player, Integer> {

    Page<Player> findAllByUserName(Pageable pageable, String username);

    List<Player> getPlayersByUserName(String username);
    List<Player> getPlayersByTeamId(int id);
}
