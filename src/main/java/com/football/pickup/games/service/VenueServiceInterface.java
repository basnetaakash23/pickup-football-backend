package com.football.pickup.games.service;

import com.football.pickup.games.dto.request.VenueDto;
import com.football.pickup.games.entity.Venue;

import java.util.List;

public interface VenueServiceInterface {

    public String addVenue(VenueDto venueDto);

    public List<VenueDto> getAllVenues();
}
