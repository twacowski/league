package com.league.repository;

import com.league.model.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeagueRepository extends JpaRepository<League, Integer> {
    List<League> getLeaguesByUserName(String username);
    List<League> findTop10ByOrderByIdDesc();
    List<League> findTop10ByOrderByNumberDesc();

    @Query("SELECT l FROM League l WHERE l.name LIKE %:phrase%")
    List<League> findLeaguesByPhrase(@Param("phrase") String phrase);

    @Query("SELECT l FROM League l WHERE l.voivodeship.id=:voivodeship AND l.county.id=:county")
    List<League> findLeaguesByLocation(@Param("voivodeship") int voivodeship, @Param("county") int county);

    @Query("SELECT l FROM League l WHERE l.voivodeship.id=:voivodeship")
    List<League> findLeaguesByVoivodeship(@Param("voivodeship") int voivodeship);
}
