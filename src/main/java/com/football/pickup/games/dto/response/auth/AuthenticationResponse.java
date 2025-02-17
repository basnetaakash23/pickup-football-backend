package com.football.pickup.games.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.football.pickup.games.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    private String token;

    @JsonIgnore
    private Users user;


}
