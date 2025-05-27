package com.football.pickup.games.service.auth;

import com.football.pickup.games.entity.Users;
import com.football.pickup.games.repository.UsersRepository;
import com.football.pickup.games.util.RoleToAuthorityConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final UsersRepository userRepository;

    public CustomUserDetailsService(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        // Fetch user from the database
        Users user = userRepository.findByName(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found: " + username);
        }

        Set<GrantedAuthority> authorities = RoleToAuthorityConverter.convertRolesToAuthorities(user.getRoles());

        // Convert to UserDetails (e.g., with roles and permissions)
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                authorities);
    }

}

