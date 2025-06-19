package com.sportygroup.jackpot.repository;

import com.sportygroup.jackpot.model.Jackpot;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of JackpotRepository using Reactive Streams.
 * Uses a ConcurrentHashMap to simulate a database table for jackpots.
 * Operations are wrapped in Mono.fromCallable to expose them as reactive streams,
 * acknowledging that the underlying HashMap operations are blocking.
 */
@Repository
public class InMemJackpotRepository implements JackpotRepository {

    private final ConcurrentHashMap<String, Jackpot> jackpots = new ConcurrentHashMap<>();

    /**
     * Saves a jackpot to the in-memory map. If a jackpot with the same ID already exists, it will be updated.
     * Wrapped in Mono.fromCallable to represent a potentially blocking operation reactively.
     * @param jackpot The jackpot to save.
     * @return A Mono emitting the saved jackpot.
     */
    @Override
    public Mono<Jackpot> save(Jackpot jackpot) {
        return Mono.fromCallable(() -> {
            jackpots.put(jackpot.getJackpotId(), jackpot);
            System.out.println("Saved Jackpot: " + jackpot.getJackpotId() + " with pool: " + jackpot.getCurrentPoolAmount());
            return jackpot;
        });
    }

    /**
     * Finds a jackpot by its ID from the in-memory map.
     * Wrapped in Mono.fromCallable to represent a potentially blocking operation reactively.
     * @param jackpotId The ID of the jackpot to find.
     * @return A Mono emitting the jackpot if found, or empty otherwise.
     */
    @Override
    public Mono<Jackpot> findById(String jackpotId) {
        return Mono.fromCallable(() -> Optional.ofNullable(jackpots.get(jackpotId)))
                .flatMap(Mono::justOrEmpty);
    }

    /**
     * Updates an existing jackpot in the in-memory map.
     * Wrapped in Mono.fromCallable to represent a potentially blocking operation reactively.
     * Uses a loop with `replace` for a basic form of optimistic locking/retries for concurrent updates,
     * which is crucial since `synchronized` is removed.
     * @param jackpot The jackpot with updated fields.
     * @return A Mono emitting the updated jackpot, or an error if update fails after retries.
     */
    @Override
    public Mono<Jackpot> update(Jackpot jackpot) {
        return Mono.defer(() -> {
            Jackpot existingJackpot = jackpots.get(jackpot.getJackpotId());
            if (existingJackpot == null) {

                System.err.println("Attempted to update non-existent jackpot: " + jackpot.getJackpotId() + ". Failing update.");
                return Mono.error(new IllegalArgumentException("Jackpot with ID " + jackpot.getJackpotId() + " not found for update."));
            }

            int retries = 3;
            for (int i = 0; i < retries; i++) {

                Jackpot currentJackpot = jackpots.get(jackpot.getJackpotId());
                if (currentJackpot == null) {
                    return Mono.error(new IllegalStateException("Jackpot disappeared during update retry: " + jackpot.getJackpotId()));
                }

                Jackpot newJackpotState = new Jackpot(
                        currentJackpot.getJackpotId(),
                        jackpot.getCurrentPoolAmount(),
                        currentJackpot.getInitialPoolValue(),
                        currentJackpot.getConfig(),
                        currentJackpot.getCreatedAt()
                );

                if (jackpots.replace(currentJackpot.getJackpotId(), currentJackpot, newJackpotState)) {
                    System.out.println("Updated Jackpot: " + newJackpotState.getJackpotId() + " new pool: " + newJackpotState.getCurrentPoolAmount());
                    return Mono.just(newJackpotState);
                } else {
                    System.out.println("Optimistic lock failure for Jackpot: " + jackpot.getJackpotId() + ". Retrying...");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return Mono.error(e);
                    }
                }
            }
            return Mono.error(new IllegalStateException("Failed to update Jackpot " + jackpot.getJackpotId() + " after " + retries + " retries due to concurrent modification."));
        });
    }
}