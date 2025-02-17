package com.football.pickup.games.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForGames {

    private Integer userId;

    private Integer gameId;
}
