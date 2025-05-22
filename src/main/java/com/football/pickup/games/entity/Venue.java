package com.football.pickup.games.entity;

import com.football.pickup.games.entity.embeddable.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Venue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private HashMap<String, List<String>> timeSlots;

    @NotNull
    @Embedded
    private Address address;

    @NotNull
    private String nameOfProperty;

    @NotNull
    private Integer price;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<Games> games = new ArrayList<>();

}
