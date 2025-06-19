package com.sportygroup.jackpot.repository;

import com.sportygroup.jackpot.model.JackpotContribution;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

/**
 * Interface for managing JackpotContribution entities.
 * This repository is for historical tracking of contributions.
 * Methods now return Reactive Streams types (Mono for 0-1 elements, Flux for 0-N elements).
 */
public interface JackpotContributionRepository {
    /**
     * Saves a jackpot contribution record reactively.
     * @param contribution The contribution record to save.
     * @return A Mono emitting the saved contribution record.
     */
    Mono<JackpotContribution> save(JackpotContribution contribution);

    /**
     * Finds all contributions for a given bet ID reactively.
     * @param betId The ID of the bet.
     * @return A Flux emitting matching contribution records.
     */
    Flux<JackpotContribution> findByBetId(String betId);

    /**
     * Finds a contribution by its Bet ID and Jackpot ID reactively.
     * @param betId The ID of the bet.
     * @param jackpotId The ID of the jackpot.
     * @return A Mono emitting the contribution if found, or empty otherwise.
     */
    Mono<JackpotContribution> findByBetIdAndJackpotId(String betId, String jackpotId);
}