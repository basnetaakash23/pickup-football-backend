package com.football.pickup.games.service;

import com.football.pickup.games.dto.request.RegisterForGames;
import com.football.pickup.games.dto.response.GameDto;
import com.football.pickup.games.entity.Games;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface GameServiceInterface {

    public void createGames();

    public String registerForGames(RegisterForGames registerForGames);

    public LocalDate findLastGames();

    public List<GameDto> getActiveGames();

    public List<GameDto> getGamesByLocalDateTime(LocalDate localDateTime);
}
