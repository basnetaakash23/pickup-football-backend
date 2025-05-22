package com.football.pickup.games.service.impl;

import com.football.pickup.games.dto.request.VenueDto;
import com.football.pickup.games.entity.Venue;
import com.football.pickup.games.entity.embeddable.Address;
import com.football.pickup.games.repository.VenueRepository;
import com.football.pickup.games.service.VenueServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueServiceInterface {

    private final VenueRepository venueRepository;

    public String addVenue(VenueDto venueDto){
        Venue venue = Venue.builder().
                price(venueDto.getPrice()).
                nameOfProperty(venueDto.getPropertyName()).
                address(Address.builder().streetAddress(venueDto.getStreetAddress()).city(venueDto.getCity()).state(venueDto.getState()).zipCode(venueDto.getZipCode()).build()).
                timeSlots(venueDto.getTimeSlots()).
                build();
        return venueRepository.save(venue).getId().toString();

    }

    public List<VenueDto> getAllVenues(){
        List<VenueDto> venue = venueRepository.findAll().stream().map(v -> buildVenueDto(v)).collect(Collectors.toList());
        return venue;
    }

    private VenueDto buildVenueDto(Venue venue){
        return VenueDto.builder().propertyName(venue.getNameOfProperty())
                .price(venue.getPrice())
                .city(venue.getAddress().getCity())
                .streetAddress(venue.getAddress().getStreetAddress())
                .zipCode(venue.getAddress().getZipCode())
                .timeSlots(venue.getTimeSlots())
                .build();
    }
}
