package com.league.repository;

import com.league.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);

    @Query("SELECT u FROM User u WHERE u.userName LIKE %:phrase%")
    List<User> findUsersByPhrase(@Param("phrase") String phrase);
}
