package com.football.pickup.games.controller;

import com.football.pickup.games.dto.request.RegisterForGames;
import com.football.pickup.games.dto.response.GameDetailDto;
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

    @GetMapping
    public void createGames(){
        gameService.createGames();
    }

    @PostMapping("/registrations")
    public ResponseEntity<String> registerForGames(@RequestBody RegisterForGames registerForGames) throws Exception {
        return ResponseEntity.ok().body(gameService.registerForGames(registerForGames));
    }

    @GetMapping(value="/last")
    public ResponseEntity<LocalDate> getLastGameDate(){
        return ResponseEntity.ok().body(gameService.findLastGames());
    }

    @GetMapping(value="/active")
    public ResponseEntity<List<GameDto>> getAllActiveGames() throws Exception {
        return ResponseEntity.ok().body(gameService.getActiveGames());
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<GameDetailDto> getGameById(@PathVariable String id) throws Exception {
        return ResponseEntity.ok().body(gameService.getGameById(id));
    }

    @GetMapping
    public ResponseEntity<List<GameDto>> getGamesByDate(@RequestParam(required = false) String gameDate) throws GameNotFoundException {

        return ResponseEntity.ok().body(gameService.getGamesByLocalDateTime(LocalDate.of(Integer.parseInt(gameDate.split("-")[0]),Integer.parseInt(gameDate.split("-")[1]), Integer.parseInt(gameDate.split("-")[2]))));
    }


}
