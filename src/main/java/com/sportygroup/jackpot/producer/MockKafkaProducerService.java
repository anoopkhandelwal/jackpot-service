package com.sportygroup.jackpot.producer;

import com.sportygroup.jackpot.consumer.KafkaConsumerService;
import com.sportygroup.jackpot.model.Bet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service responsible for "publishing" bets to a Kafka topic.
 * In this assignment, it's a mock implementation that simply logs the payload
 * and immediately triggers the "consumer" logic, as a real Kafka setup is
 * not required for the assignment.
 */
@Service
public class MockKafkaProducerService implements KafkaProducerService {

    private final KafkaConsumerService kafkaConsumerService;

    /**
     * Constructor for dependency injection.
     * @param kafkaConsumerService The mock Kafka consumer service.
     */
    @Autowired
    public MockKafkaProducerService(KafkaConsumerService kafkaConsumerService) {
        this.kafkaConsumerService = kafkaConsumerService;
    }

    /**
     * Simulates publishing a bet to a Kafka topic.
     * In this mocked setup, it directly calls the consumer service.
     *
     * @param bet The bet to publish.
     * @return A Mono that completes when the "publishing" and "consumption" process is done.
     */
    public Mono<Void> publishBet(Bet bet) {
        System.out.println("KafkaProducerService: Mock publishing bet to topic 'jackpot-bets': " + bet.getBetId());

        return Mono.just(bet)
                .flatMap(kafkaConsumerService::consumeBet)
                .doOnError(e -> System.err.println("KafkaProducerService: Error during mock publishing/consuming: " + e.getMessage()));
    }
}