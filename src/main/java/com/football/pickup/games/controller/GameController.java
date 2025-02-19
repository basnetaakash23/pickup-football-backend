package com.football.pickup.games.controller;

import com.football.pickup.games.dto.request.RegisterForGames;
import com.football.pickup.games.dto.response.GameDto;
import com.football.pickup.games.entity.Games;
import com.football.pickup.games.exceptions.GameNotFoundException;
import com.football.pickup.games.service.GameServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/games")
public class GameController {

    private final GameServiceInterface gameService;

    @PostMapping(value="/register-games")
    public ResponseEntity<String> registerForGames(@RequestBody RegisterForGames registerForGames) throws Exception {
        return ResponseEntity.ok().body(gameService.registerForGames(registerForGames));
    }

    @GetMapping(value="/get-last-game")
    public ResponseEntity<LocalDate> getLastGameDate(){
        return ResponseEntity.ok().body(gameService.findLastGames());
    }

    @GetMapping(value="/get-active-games")
    public ResponseEntity<List<GameDto>> getAllActiveGames() throws Exception {
        return ResponseEntity.ok().body(gameService.getActiveGames());
    }

    @GetMapping(value="/create-games")
    public void createGames(){
        gameService.createGames();
    }

    @GetMapping(value="/get-game-date/{gameDate}")
    public ResponseEntity<List<GameDto>> getGamesByDate(@PathVariable String gameDate) throws GameNotFoundException {

        return ResponseEntity.ok().body(gameService.getGamesByLocalDateTime(LocalDate.of(Integer.parseInt(gameDate.split("-")[0]),Integer.parseInt(gameDate.split("-")[1]), Integer.parseInt(gameDate.split("-")[2]))));
    }


}
