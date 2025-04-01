package com.football.pickup.games.repository;

import com.football.pickup.games.entity.Games;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Games, Long> {

//    @Query("SELECT g FROM Games g ORDER BY g.localDateTime DESC LIMIT 1")
//    Optional<Games> findTopByOrderByLocalDateTimeDesc();
Optional<Games> findTopByOrderByLocalDateTimeDesc();

Optional<List<Games>> findByLocalDateTimeGreaterThanEqual(LocalDateTime localDateTime);

    Optional<List<Games>> findByLocalDateTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<List<Games>> findByIsAvailable(Boolean isAvailable);

    Optional<Games> findGameById(Long id);
}
