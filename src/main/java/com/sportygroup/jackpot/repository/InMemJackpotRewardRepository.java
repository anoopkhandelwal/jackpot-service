package com.sportygroup.jackpot.repository;

import com.sportygroup.jackpot.model.JackpotReward;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of JackpotRewardRepository using Reactive Streams.
 * Uses a ConcurrentHashMap to store reward records for historical purposes.
 * Operations are wrapped in Mono.fromCallable to expose them as reactive streams,
 * acknowledging that the underlying HashMap operations are blocking.
 */
@Repository
public class InMemJackpotRewardRepository implements JackpotRewardRepository {

    private final ConcurrentHashMap<String, JackpotReward> rewards = new ConcurrentHashMap<>();

    /**
     * Saves a jackpot reward record to the in-memory map.
     * Wrapped in Mono.fromCallable to represent a potentially blocking operation reactively.
     * @param reward The reward record to save.
     * @return A Mono emitting the saved reward record.
     */
    @Override
    public Mono<JackpotReward> save(JackpotReward reward) {
        return Mono.fromCallable(() -> {
            String key = generateKey(reward.getBetId(), reward.getJackpotId());
            rewards.put(key, reward);
            System.out.println("Saved JackpotReward: BetID=" + reward.getBetId() + ", JackpotID=" + reward.getJackpotId() + ", Amount=" + reward.getJackpotRewardAmount());
            return reward;
        });
    }

    /**
     * Finds all rewards for a given bet ID.
     * Wrapped in Flux.fromIterable and Mono.fromCallable to represent a potentially blocking operation reactively.
     * @param betId The ID of the bet.
     * @return A Flux emitting matching reward records.
     */
    @Override
    public Flux<JackpotReward> findByBetId(String betId) {
        return Mono.fromCallable(() -> rewards.values().stream()
                        .filter(r -> r.getBetId().equals(betId))
                        .collect(Collectors.toList()))
                .flatMapMany(Flux::fromIterable);
    }

    /**
     * Finds a reward by its Bet ID and Jackpot ID.
     * Wrapped in Mono.fromCallable to represent a potentially blocking operation reactively.
     * @param betId The ID of the bet.
     * @param jackpotId The ID of the jackpot.
     * @return A Mono emitting the reward if found, or empty otherwise.
     */
    @Override
    public Mono<JackpotReward> findByBetIdAndJackpotId(String betId, String jackpotId) {
        return Mono.fromCallable(() -> {
            String key = generateKey(betId, jackpotId);
            return Optional.ofNullable(rewards.get(key));
        }).flatMap(Mono::justOrEmpty);
    }

    /**
     * Generates a unique key for the ConcurrentHashMap using betId and jackpotId.
     * @param betId The bet ID.
     * @param jackpotId The jackpot ID.
     * @return A concatenated string key.
     */
    private String generateKey(String betId, String jackpotId) {
        return betId + "_" + jackpotId;
    }
}