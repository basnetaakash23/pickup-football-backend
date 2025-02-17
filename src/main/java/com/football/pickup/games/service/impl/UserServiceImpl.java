package com.football.pickup.games.service.impl;

import com.football.pickup.games.dto.request.UserDto;
import com.football.pickup.games.dto.response.auth.AuthenticationResponse;
import com.football.pickup.games.entity.Roles;
import com.football.pickup.games.entity.Users;
import com.football.pickup.games.entity.enums.Role;
import com.football.pickup.games.repository.RoleRepository;
import com.football.pickup.games.repository.UsersRepository;
import com.football.pickup.games.service.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserServiceInterface {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public AuthenticationResponse addUser(UserDto userDto) throws Exception {
        Users user = usersRepository.findByEmail(userDto.getEmail());
        log.info("User={}",user);
        if(user!=null){
            throw new Exception("User already exists exception");
        }
        Users userObject = new Users();
        userObject.setName(userDto.getName());
        userObject.setEmail(userDto.getEmail());
        userObject.setPhoneNumber(userDto.getPhoneNumber());
        userObject.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Assign default role
        Roles defaultRole = roleRepository.findByName("ROLE_PLAYER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        userObject.setRoles(Set.of(defaultRole));

        Users user_ = usersRepository.save(userObject);

        return AuthenticationResponse.builder().user(user_).build();

    }
}
