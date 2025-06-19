package com.sportygroup.jackpot.service;

import com.sportygroup.jackpot.model.Bet;
import com.sportygroup.jackpot.producer.KafkaProducerService;
import com.sportygroup.jackpot.repository.BetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service responsible for managing bets.
 * It handles saving the bet to the repository and then "publishing" it to Kafka (mocked).
 * The processing logic (contribution, reward) is now handled by the KafkaConsumerService.
 */
@Service
public class BetService {

    private final BetRepository betRepository;
    private final KafkaProducerService kafkaProducerService;

    /**
     * Constructor for dependency injection.
     * @param betRepository The repository for managing bets.
     * @param kafkaProducerService The Kafka producer service.
     */
    @Autowired
    public BetService(BetRepository betRepository, KafkaProducerService kafkaProducerService) {
        this.betRepository = betRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * Publishes a bet to the system. This involves saving the bet and then
     * "publishing" it to Kafka. The actual processing logic (jackpot contribution/reward)
     * is triggered by the KafkaConsumerService, which is called internally by the
     * KafkaProducerService in this mocked setup.
     *
     * @param bet The bet to publish.
     * @return A Mono emitting the saved Bet, or an error if the operation fails.
     */
    public Mono<Bet> publishBet(Bet bet) {

        return betRepository.save(bet)
                .doOnSuccess(savedBet -> System.out.println("BetService: Bet saved: " + savedBet.getBetId()))
                .flatMap(savedBet -> kafkaProducerService.publishBet(savedBet).thenReturn(savedBet))
                .doOnError(e -> System.err.println("BetService: Error saving or publishing bet: " + e.getMessage()));
    }
}