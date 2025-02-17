package com.football.pickup.games.util;

import com.football.pickup.games.entity.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public class RoleToAuthorityConverter {

    public static Set<GrantedAuthority> convertRolesToAuthorities(Set<Roles> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // Map role names to authorities
                .collect(Collectors.toSet());
    }
}
