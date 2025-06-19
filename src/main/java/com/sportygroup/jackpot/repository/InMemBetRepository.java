package com.sportygroup.jackpot.repository;

import com.sportygroup.jackpot.model.Bet;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of BetRepository using Reactive Streams.
 * Uses a ConcurrentHashMap to simulate a database table for bets.
 * Operations are wrapped in Mono.fromCallable to expose them as reactive streams,
 * acknowledging that the underlying HashMap operations are blocking.
 */
@Repository
public class InMemBetRepository implements BetRepository {

    private final ConcurrentHashMap<String, Bet> bets = new ConcurrentHashMap<>();

    /**
     * Saves a bet to the in-memory map. If a bet with the same ID already exists, it will be updated.
     * Wrapped in Mono.fromCallable to represent a potentially blocking operation reactively.
     * @param bet The bet to save.
     * @return A Mono emitting the saved bet.
     */
    @Override
    public Mono<Bet> save(Bet bet) {
        return Mono.fromCallable(() -> {
            bets.put(bet.getBetId(), bet);
            System.out.println("Saved Bet: " + bet.getBetId());
            return bet;
        });
    }

    /**
     * Finds a bet by its ID from the in-memory map.
     * Wrapped in Mono.fromCallable to represent a potentially blocking operation reactively.
     * @param betId The ID of the bet to find.
     * @return A Mono emitting the bet if found, or empty otherwise.
     */
    @Override
    public Mono<Bet> findById(String betId) {
        return Mono.fromCallable(() -> Optional.ofNullable(bets.get(betId)))
                .flatMap(Mono::justOrEmpty); // Converts Optional<Bet> to Mono<Bet>
    }

    /**
     * Finds all bets from the in-memory map.
     * Wrapped in Flux.fromIterable and Mono.fromCallable to represent a potentially blocking operation reactively.
     * @return A Flux emitting all bets.
     */
    @Override
    public Flux<Bet> findAll() {
        return Mono.fromCallable(bets::values)
                .flatMapMany(Flux::fromIterable);
    }
}