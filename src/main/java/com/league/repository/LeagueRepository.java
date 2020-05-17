package com.league.repository;

import com.league.model.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeagueRepository extends JpaRepository<League, Integer> {
    List<League> getLeaguesByUserNameOrderByIdDesc(String username);
    List<League> findTop5ByOrderByIdDesc();
    List<League> findTop5ByOrderByNumberDesc();

    @Query("SELECT l FROM League l WHERE l.name LIKE %:phrase% AND l.status <> 'INACTIVE' ORDER BY l.id DESC")
    List<League> findLeaguesByPhrase(@Param("phrase") String phrase);

    @Query("SELECT l FROM League l WHERE l.status <> 'INACTIVE' ORDER BY l.id DESC")
    List<League> getAllLeaguesToView();

    @Query("SELECT l FROM League l WHERE l.voivodeship.id=:voivodeship AND l.county.id=:county ORDER BY l.id DESC")
    List<League> findLeaguesByLocation(@Param("voivodeship") int voivodeship, @Param("county") int county);

    @Query("SELECT l FROM League l WHERE l.voivodeship.id=:voivodeship ORDER BY l.id DESC")
    List<League> findLeaguesByVoivodeship(@Param("voivodeship") int voivodeship);
}
