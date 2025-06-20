package com.sportygroup.jackpot.repository;

import com.sportygroup.jackpot.model.JackpotContribution;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of JackpotContributionRepository.
 * Uses a ConcurrentHashMap to store contribution records for historical purposes.
 * Operations are wrapped in Mono.fromCallable to expose them as reactive streams,
 * acknowledging that the underlying HashMap operations are blocking.
 */
@Repository
public class InMemJackpotContributionRepository implements JackpotContributionRepository {

    private final ConcurrentHashMap<String, JackpotContribution> contributions = new ConcurrentHashMap<>();

    /**
     * Saves a jackpot contribution record to the in-memory map.
     * @param contribution The contribution record to save.
     * @return A Mono emitting the saved contribution record.
     */
    @Override
    public Mono<JackpotContribution> save(JackpotContribution contribution) {
        return Mono.fromCallable(() -> {
            String key = generateKey(contribution.getBetId(), contribution.getJackpotId());
            contributions.put(key, contribution);
            System.out.println("Saved JackpotContribution: BetID=" + contribution.getBetId() + ", JackpotID=" + contribution.getJackpotId() + ", Amount=" + contribution.getContributionAmount());
            return contribution;
        });
    }

    /**
     * Finds all contributions for a given bet ID.
     * @param betId The ID of the bet.
     * @return A Flux emitting matching contribution records.
     */
    @Override
    public Flux<JackpotContribution> findByBetId(String betId) {
        return Mono.fromCallable(() -> contributions.values().stream()
                        .filter(c -> c.getBetId().equals(betId))
                        .collect(Collectors.toList()))
                .flatMapMany(Flux::fromIterable);
    }

    /**
     * Finds a contribution by its Bet ID and Jackpot ID.
     * @param betId The ID of the bet.
     * @param jackpotId The ID of the jackpot.
     * @return A Mono emitting the contribution if found, or empty otherwise.
     */
    @Override
    public Mono<JackpotContribution> findByBetIdAndJackpotId(String betId, String jackpotId) {
        return Mono.fromCallable(() -> {
            String key = generateKey(betId, jackpotId);
            return Optional.ofNullable(contributions.get(key));
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