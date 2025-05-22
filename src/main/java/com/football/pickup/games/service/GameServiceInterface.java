package com.football.pickup.games.service;

import com.football.pickup.games.dto.request.RegisterForGames;
import com.football.pickup.games.dto.response.GameDetailDto;
import com.football.pickup.games.dto.response.GameDto;
import com.football.pickup.games.entity.Games;
import com.football.pickup.games.exceptions.GameNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface GameServiceInterface {

    void createGames();

    String registerForGames(RegisterForGames registerForGames) throws Exception;

    LocalDate findLastGames();

    List<GameDto> getActiveGames() throws Exception;

    List<GameDto> getGamesByLocalDateTime(LocalDate localDateTime) throws GameNotFoundException;

    GameDetailDto getGameById(String id);
}
