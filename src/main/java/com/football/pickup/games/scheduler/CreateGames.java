package com.football.pickup.games.scheduler;

import com.football.pickup.games.service.GameServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateGames {

    private final GameServiceInterface gameServiceInterface;

    @Scheduled(cron = "0 0 0 * * ?")  // Every day at midnight
    public void runTask() {
        gameServiceInterface.createGames();

    }


}
