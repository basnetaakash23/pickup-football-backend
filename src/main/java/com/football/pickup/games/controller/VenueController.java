package com.football.pickup.games.controller;

import com.football.pickup.games.dto.request.VenueDto;
import com.football.pickup.games.entity.Venue;
import com.football.pickup.games.service.VenueServiceInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/venues")
public class VenueController {

    private final VenueServiceInterface venueService;

    @GetMapping
    public ResponseEntity<List<VenueDto>> getAllVenues(){
        return ResponseEntity.ok().body(venueService.getAllVenues());
    }

    @PostMapping
    public ResponseEntity<String> addVenues(@RequestBody @Valid VenueDto venueDto){
        return ResponseEntity.ok().body(venueService.addVenue(venueDto));
    }

}
