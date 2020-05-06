package com.league.repository;

import com.league.model.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatRepository extends JpaRepository<Stat, Integer> {

    @Query("SELECT s FROM Stat s WHERE s.league.id=:league AND s.player.id=:player")
    Stat getPlayerStat(@Param("league") int leagueId, @Param("player") int playerId);

    @Query("SELECT s FROM Stat s WHERE s.league.id=:leagueId AND s.goals > 0 order by s.goals desc")
    List<Stat> getTopScorers(@Param("leagueId") int leagueId);

    @Query("SELECT s FROM Stat s WHERE s.league.id=:leagueId AND s.ownGoals > 0 order by s.ownGoals desc")
    List<Stat> getMostOwnGoals(@Param("leagueId") int leagueId);

    @Query("SELECT s FROM Stat s WHERE s.league.id=:leagueId AND s.yellowCards > 0 order by s.yellowCards desc")
    List<Stat> getMostYellowCards(@Param("leagueId") int leagueId);

    @Query("SELECT s FROM Stat s WHERE s.league.id=:leagueId AND s.redCards > 0 order by s.redCards desc")
    List<Stat> getMostRedCards(@Param("leagueId") int leagueId);
}
