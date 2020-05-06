package com.league.repository;

import com.league.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
    void deleteById(int id);

    @Query("SELECT p FROM Participation p WHERE p.league.id=:league AND p.team.id=:team")
    Participation getParticipation(@Param("league") int leagueId, @Param("team") int teamId);

}
