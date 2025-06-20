package com.sportygroup.jackpot.repository;

import com.sportygroup.jackpot.model.JackpotReward;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

/**
 * Interface for managing JackpotReward entities.
 * This repository is for historical tracking of rewards.
 * Methods now return Reactive Streams types (Mono for 0-1 elements, Flux for 0-N elements).
 */
public interface JackpotRewardRepository {
    /**
     * Saves a jackpot reward record.
     * @param reward The reward record to save.
     * @return A Mono emitting the saved reward record.
     */
    Mono<JackpotReward> save(JackpotReward reward);

    /**
     * Finds all rewards for a given bet ID.
     * @param betId The ID of the bet.
     * @return A Flux emitting matching reward records.
     */
    Flux<JackpotReward> findByBetId(String betId);

    /**
     * Finds a reward by its Bet ID and Jackpot ID.
     * @param betId The ID of the bet.
     * @param jackpotId The ID of the jackpot.
     * @return A Mono emitting the reward if found, or empty otherwise.
     */
    Mono<JackpotReward> findByBetIdAndJackpotId(String betId, String jackpotId);
}