package com.football.pickup.games.service;

import com.football.pickup.games.dto.request.UserDto;
import com.football.pickup.games.dto.response.auth.AuthenticationResponse;

public interface UserServiceInterface {

    AuthenticationResponse addUser(UserDto user) throws Exception;
}
