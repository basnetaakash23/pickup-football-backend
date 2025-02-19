package com.football.pickup.games.controller;

import com.football.pickup.games.dto.request.UserDto;
import com.football.pickup.games.dto.request.auth.LoginRequest;
import com.football.pickup.games.dto.response.auth.AuthenticationResponse;
import com.football.pickup.games.repository.UsersRepository;
import com.football.pickup.games.service.JwtService;
import com.football.pickup.games.service.UserServiceInterface;
import com.football.pickup.games.util.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UsersRepository usersRepository;
    private final UserServiceInterface userService;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;


    @PostMapping(value="/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserDto userDto) throws Exception {
        try{
            AuthenticationResponse authenticationResponse = userService.addUser(userDto);
            String jwtToken = jwtService.generateToken(authenticationResponse);
            log.info("JWT Token = {}", jwtToken);
            authenticationResponse.setToken(jwtToken);
            return ResponseEntity.ok(authenticationResponse);

        }catch(Exception ex){
            return ResponseEntity.internalServerError().body(ex.getLocalizedMessage());
        }

    }

    @PostMapping(value="/login")
    public String login(@RequestBody LoginRequest loginRequest) throws Exception{
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return jwtService.generateToken(authentication);
        }catch(Exception ex){
            throw new Exception("Invalid username or password");
        }
    }

    @GetMapping(value="/get")
    public ResponseEntity<String> getSomething(){
        return ResponseEntity.ok().body("OK");
    }

    @GetMapping(value="/get-current-user")
    public ResponseEntity<String> getCurrentUser(){
        return ResponseEntity.ok().body(UserUtils.getCurrentUsername());

    }

//    @GetMapping(value="/login")
//    public ResponseEntity<String> loginSomething(){
//        return ResponseEntity.ok().body("OK");
//    }

    @GetMapping(value="/register-user")
    public UserDto registerUserDto(@RequestBody UserDto userDto){
        return userDto;
    }
}
