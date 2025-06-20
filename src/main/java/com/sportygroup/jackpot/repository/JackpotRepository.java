package com.sportygroup.jackpot.repository;

import com.sportygroup.jackpot.model.Jackpot;
import reactor.core.publisher.Mono;

/**
 * Interface for managing Jackpot entities.
 * Adheres to Dependency Inversion Principle.
 * Methods now return Reactive Streams types (Mono for 0-1 elements).
 */
public interface JackpotRepository {
    /**
     * Saves a jackpot.
     * @param jackpot The jackpot to save.
     * @return A Mono emitting the saved jackpot.
     */
    Mono<Jackpot> save(Jackpot jackpot);

    /**
     * Finds a jackpot by its ID.
     * @param jackpotId The ID of the jackpot to find.
     * @return A Mono emitting the jackpot if found, or empty otherwise.
     */
    Mono<Jackpot> findById(String jackpotId);

    /**
     * Updates an existing jackpot.
     * @param jackpot The jackpot with updated fields.
     * @return A Mono emitting the updated jackpot, or an error if not found.
     */
    Mono<Jackpot> update(Jackpot jackpot);
}