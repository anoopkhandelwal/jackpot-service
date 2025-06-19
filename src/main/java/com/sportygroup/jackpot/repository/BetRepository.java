package com.sportygroup.jackpot.repository;

import com.sportygroup.jackpot.model.Bet;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

/**
 * Interface for managing Bet entities.
 * Adheres to Dependency Inversion Principle, allowing different implementations (e.g., in-memory, database).
 */
public interface BetRepository {
    /**
     * Saves a bet.
     * @param bet The bet to save.
     * @return A Mono emitting the saved bet.
     */
    Mono<Bet> save(Bet bet);

    /**
     * Finds a bet by its ID way.
     * @param betId The ID of the bet to find.
     * @return A Mono emitting the bet if found, or empty otherwise.
     */
    Mono<Bet> findById(String betId);

    /**
     * Finds all bets.
     * @return A Flux emitting all bets.
     */
    Flux<Bet> findAll();
}