package com.football.pickup.games.repository;

import com.football.pickup.games.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    public Users findByEmail(String email);

    public Users findByName(String name);
}
