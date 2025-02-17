package com.football.pickup.games.dto.response;

import com.football.pickup.games.entity.Users;
import com.football.pickup.games.entity.Venue;
import com.football.pickup.games.entity.embeddable.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {

    private Long id;

    private LocalDateTime localDateTime;

    private List<Users> users;

    private Address address;
    private boolean isAvailable;
    private Integer price;
}
