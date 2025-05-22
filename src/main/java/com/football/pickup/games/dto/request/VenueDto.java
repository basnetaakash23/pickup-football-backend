package com.football.pickup.games.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class VenueDto {

    private String streetAddress;

    private String city;

    private String state;

    private String zipCode;

    private String propertyName;

    private Integer price;

    private HashMap<String, List<String>> timeSlots;

}
