package com.football.pickup.games.entity;

import com.football.pickup.games.entity.embeddable.Address;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Games implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime localDateTime;

    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(
            name = "game_users",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Users> users;

    @ManyToOne(fetch = FetchType.LAZY) // Many games can be associated with one venue
    @JoinColumn(name = "venue_id", nullable = false) // Creates a foreign key column in the "games" table
    private Venue venue;

    private boolean isAvailable;

}
