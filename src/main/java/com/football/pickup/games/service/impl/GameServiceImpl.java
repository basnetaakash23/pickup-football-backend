package com.football.pickup.games.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.football.pickup.games.dto.request.RegisterForGames;
import com.football.pickup.games.dto.response.GameDetailDto;
import com.football.pickup.games.dto.response.GameDto;
import com.football.pickup.games.entity.Games;
import com.football.pickup.games.entity.Users;
import com.football.pickup.games.entity.Venue;
import com.football.pickup.games.exceptions.GameNotFoundException;
import com.football.pickup.games.repository.GameRepository;
import com.football.pickup.games.repository.UsersRepository;
import com.football.pickup.games.repository.VenueRepository;
import com.football.pickup.games.service.GameServiceInterface;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @Transactional
    @Retryable(value = OptimisticLockException.class, maxAttempts = 3)
    public String registerForGames(RegisterForGames registerForGames){
        Integer gameId = registerForGames.getGameId();
        Optional<Games> games = gameRepository.findById(Long.valueOf(gameId));
        if(games.isEmpty()){
            throw new GameNotFoundException("Game does not exists");
        }

        Optional<Users> user = usersRepository.findById(Long.valueOf(registerForGames.getUserId()));
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }
        Games game = games.get();
        game.addUser(user.get());

        return game.getId().toString();

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

    @Cacheable(value="games")
    @Override
    public List<GameDto> getActiveGames()  {
        //LocalDateTime localDateTime = LocalDateTime.now();
        Optional<List<Games>> games  = gameRepository.findByIsAvailable(true);
        if(games.isEmpty()){
            throw new GameNotFoundException("Game not found");
        }
        List<GameDto> allCurrentFutureGames = games.get().stream().map(this::buildCurrentFutureGames).collect(Collectors.toList());
        return allCurrentFutureGames;

    }

    @Override
    public List<GameDto> getGamesByLocalDateTime(LocalDate localDate){
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.atTime(LocalTime.MAX);
        Optional<List<Games>> games = gameRepository.findByLocalDateTimeBetween(startOfDay, endOfDay);
        if(games.isEmpty() || games.get().size()==0){
            throw new GameNotFoundException("No games found for local date="+localDate.toString());
        }
        List<GameDto> todaysGames = games.get().stream().map(this::buildCurrentFutureGames).collect(Collectors.toList());
        return todaysGames;

    }

    @Override
    public GameDetailDto getGameById(String id) {
        Optional<Games> game = gameRepository.findById(Long.valueOf(id));
        if(game.isEmpty()){
            throw new GameNotFoundException("No games found for id="+id);
        }

        Games gameObj = game.get();

        GameDetailDto gameDetailDto = GameDetailDto.builder().
                id(gameObj.getId())
                .address(gameObj.getVenue().getAddress())
                .localDateTime(gameObj.getLocalDateTime())
                .users(gameObj.getUsers())
                .isAvailable(gameObj.isAvailable())
                .price(gameObj.getVenue().getPrice())
                .propertyName(gameObj.getVenue().getNameOfProperty())
                .signIn(gameObj.getUsers().size())
                .propertyDescription("This is a 6 X 6 turf fields so we do not recommend putting on metal studs. Please park your vehicle at the available parking spot. Entrance is on the south side of the building. There are rest rooms in the property. Also we have fountains for clean drinking water. In case of injury, please reach out to front desk staff for safety kit.") //hard coded for now
                .totalPlayers(Integer.valueOf(12)).  //hard coded for now
                build();

        return gameDetailDto;
    }

    private GameDto buildCurrentFutureGames(Games games) {
        return GameDto.builder().id(games.getId())
                .address(games.getVenue().getAddress())
                .localDateTime(games.getLocalDateTime())
                .users(games.getUsers())
                .isAvailable(games.isAvailable())
                .price(games.getVenue().getPrice())
                .propertyName(games.getVenue().getNameOfProperty())
                .signIn(games.getUsers().size())
                .totalPlayers(Integer.valueOf(12))
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
