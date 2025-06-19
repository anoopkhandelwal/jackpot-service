package com.sportygroup.jackpot.producer;

import com.sportygroup.jackpot.model.Bet;
import reactor.core.publisher.Mono;

/**
 * Interface for publishing bets to Kafka reactively.
 * This interface abstracts the underlying messaging system, allowing for
 * different implementations (e.g., real Kafka, mock Kafka).
 * Methods now return Reactive Streams types (Mono).
 */
public interface KafkaProducerService {
    /**
     * Publishes a bet to the configured Kafka topic.
     * @param bet The bet object to be published.
     * @return A Mono that completes when the bet is successfully published.
     */
    Mono<Void> publishBet(Bet bet);
}