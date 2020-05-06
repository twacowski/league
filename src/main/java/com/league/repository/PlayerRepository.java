package com.league.repository;

import com.league.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends PagingAndSortingRepository<Player, Integer> {

    Page<Player> findAllByUserName(Pageable pageable, String username);
    List<Player> getPlayersByUserName(String username);
    List<Player> getPlayersByTeamId(int id);

    @Query("SELECT p FROM Player p WHERE p.name LIKE %:phrase%")
    List<Player> findPlayersByPhrase(@Param("phrase") String phrase);
}
