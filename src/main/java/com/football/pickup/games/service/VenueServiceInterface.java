package com.football.pickup.games.service;

import com.football.pickup.games.dto.request.VenueDto;
import com.football.pickup.games.entity.Venue;

import java.util.List;

public interface VenueServiceInterface {

    String addVenue(VenueDto venueDto);

    List<VenueDto> getAllVenues();
}
