package com.football.pickup.games.dto.response;

import com.football.pickup.games.entity.Users;
import com.football.pickup.games.entity.embeddable.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameDetailDto {

    private Long id;

    private LocalDateTime localDateTime;

    private List<Users> users;

    private Address address;
    private boolean isAvailable;
    private Integer price;
    private String propertyName;
    private Integer signIn;
    private Integer totalPlayers;
    private String propertyDescription;

}
