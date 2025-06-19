package com.sportygroup.jackpot.api.controller;

import com.sportygroup.jackpot.api.controller.request.BetRequest;
import com.sportygroup.jackpot.model.Bet;
import com.sportygroup.jackpot.service.BetService;
import com.sportygroup.jackpot.util.IdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * REST Controller for handling bet-related API requests.
 * This acts as the API endpoint to publish a bet.
 * Endpoints now return Mono<ResponseEntity<T>> for non-blocking operations.
 */
@RestController
@RequestMapping("/api/bets")
public class BetController {

    private final BetService betService;

    /**
     * Constructor for dependency injection.
     * Spring automatically injects the required service beans.
     * @param betService The bet service.
     */
    @Autowired
    public BetController(BetService betService) {
        this.betService = betService;
    }

    /**
     * API endpoint to publish a bet to Kafka (mocked).
     *
     * @param request The BetRequest containing bet details.
     * @return A Mono emitting a ResponseEntity indicating success or failure.
     */
    @PostMapping
    public Mono<ResponseEntity<Bet>> publishBet(@RequestBody BetRequest request) {
        if (request.getUserId() == null || request.getUserId().isBlank() ||
                request.getJackpotId() == null || request.getJackpotId().isBlank() ||
                request.getBetAmount() == null || request.getBetAmount().signum() <= 0) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        Bet bet = new Bet(
                IdGenerator.generateId(),
                request.getUserId(),
                request.getJackpotId(),
                request.getBetAmount(),
                LocalDateTime.now()
        );

        return betService.publishBet(bet)
                .map(savedBet -> new ResponseEntity<>(savedBet, HttpStatus.CREATED))
                .onErrorResume(e -> {
                    System.err.println("Error publishing bet: " + e.getMessage());
                    e.printStackTrace();
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}