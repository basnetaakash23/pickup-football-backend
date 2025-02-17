package com.football.pickup.games.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.football.pickup.games.Emails.EmailServiceImplementation;
import com.football.pickup.games.dto.request.RegisterForGames;
import com.football.pickup.games.dto.response.GameDto;
import com.football.pickup.games.entity.Games;
import com.football.pickup.games.entity.Users;
import com.football.pickup.games.entity.Venue;
import com.football.pickup.games.exceptions.GameNotFoundException;
import com.football.pickup.games.repository.GameRepository;
import com.football.pickup.games.repository.UsersRepository;
import com.football.pickup.games.repository.VenueRepository;
import com.football.pickup.games.service.GameServiceInterface;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameServiceInterface {

    private final GameRepository gameRepository;
    private final VenueRepository venueRepository;
    private final UsersRepository usersRepository;

    private static final int UPTO_DAY = 14;

    private final EmailServiceImplementation emailServiceImplementation;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void createGames() {
        Games games = new Games();
        LocalDate lastGameDate = findLastGames();
        LocalDate startDate = (lastGameDate != null) ? lastGameDate.plusDays(1) : LocalDate.now();

        for (int day = 0; day < UPTO_DAY; day++) { // Assuming you're creating new games for the next 14 days
            LocalDate newGameDate = startDate.plusDays(day);
            createNewGames(newGameDate, games); // Use the `newGameDate` for creating new games
        }

    }

    @Override
    public String registerForGames(RegisterForGames registerForGames) {
        Integer gameId = registerForGames.getGameId();
        Optional<Games> games = gameRepository.findById(Long.valueOf(gameId));
        if(games.isEmpty()){
            throw new RuntimeException("Game does not exists");
        }

        Optional<Users> user = usersRepository.findById(Long.valueOf(registerForGames.getUserId()));
        if(user.isEmpty()){
            throw new RuntimeException("User not found");
        }
        Games game = games.get();
        List<Users> users = game.getUsers();
        users.add(user.get());
        game.setUsers(users);
        emailServiceImplementation.sendEmail(user.get().getEmail(),game.getLocalDateTime().toString(),"User registered succesfully");
        return gameRepository.save(game).getId().toString();

    }

    @Override
    public LocalDate findLastGames() {
        Optional<Games> game = gameRepository.findTopByOrderByLocalDateTimeDesc();
        if(game.isPresent()){
            log.info("Game = {}", game);
            return game.get().getLocalDateTime().toLocalDate();
        }
        return null;

    }

    @Override
    public List<GameDto> getActiveGames() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Optional<List<Games>> games  = gameRepository.findByLocalDateTimeGreaterThanEqual(localDateTime);
        if(games.isEmpty()){
            throw new RuntimeException("No games found");
        }
        List<GameDto> allCurrentFutureGames = games.get().stream().map(this::buildCurrentFutureGames).collect(Collectors.toList());
        return allCurrentFutureGames;

    }

    @Override
    public List<GameDto> getGamesByLocalDateTime(LocalDate localDate) {
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        Optional<List<Games>> games = gameRepository.findByLocalDateTimeBetween(startOfDay, endOfDay);
        if(games.isEmpty() || games.get().size()==0){
            throw new GameNotFoundException("No games found for local date="+localDate.toString());
        }
        List<GameDto> todaysGames = games.get().stream().map(this::buildCurrentFutureGames).collect(Collectors.toList());
        return todaysGames;

    }

    private GameDto buildCurrentFutureGames(Games games) {
        return GameDto.builder().id(games.getId())
                .address(games.getVenue().getAddress())
                .localDateTime(games.getLocalDateTime())
                .users(games.getUsers())
                .isAvailable(games.isAvailable())
                .price(games.getVenue().getPrice())
                .build();
    }
    private void createNewGames(LocalDate localDate, Games games){
        List<Venue> allVenues = venueRepository.findAll();
        // Prepare to collect all games to save in batch
        List<Games> gamesToSave = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");
        for(Venue venue: allVenues){
            HashMap<String, List<String>> timeSlots = venue.getTimeSlots();

            if(timeSlots!=null){
                for (Map.Entry<String, List<String>> entry : timeSlots.entrySet()) {

                    String day_ = entry.getKey().toString();   // Access the key
                    List<String> availableTimes = entry.getValue(); // Access the value

                    for(String time: availableTimes){
                        try {
                            LocalTime time_ = LocalTime.parse(time, timeFormatter);
                            LocalDateTime dateTime = localDate.with(java.time.DayOfWeek.valueOf(day_)).atTime(time_);
                            log.info("Local Date Time: {}", dateTime);
                            gamesToSave.add(Games.builder().localDateTime(dateTime).isAvailable(true).venue(venue).build());
                        }catch(Exception ex){
                            log.error("Failed to create game for time slot =  {} with exception = {}", time, ex.getLocalizedMessage());


                        }

                    }
                }

            }
        }

        // Save all games in batch after processing all venues and time slots
        if (!gamesToSave.isEmpty()) {
            gameRepository.saveAll(gamesToSave); // Assuming saveAll supports batch saving
        }
    }

}
