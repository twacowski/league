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

    @Query("SELECT p FROM Player p WHERE p.team.league.id=:leagueId AND p.goals > 0 order by p.goals desc")
    List<Player> getTopScorers(@Param("leagueId") int leagueId);

    @Query("SELECT p FROM Player p WHERE p.team.league.id=:leagueId AND p.ownGoals > 0 order by p.ownGoals desc")
    List<Player> getMostOwnGoals(@Param("leagueId") int leagueId);

    @Query("SELECT p FROM Player p WHERE p.team.league.id=:leagueId AND p.yellowCards > 0 order by p.yellowCards desc")
    List<Player> getMostYellowCards(@Param("leagueId") int leagueId);

    @Query("SELECT p FROM Player p WHERE p.team.league.id=:leagueId AND p.redCards > 0 order by p.redCards desc")
    List<Player> getMostRedCards(@Param("leagueId") int leagueId);

    @Query("SELECT p FROM Player p WHERE p.name LIKE %:phrase%")
    List<Player> findPlayersByPhrase(@Param("phrase") String phrase);
}
