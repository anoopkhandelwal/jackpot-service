package com.sportygroup.jackpot.consumer;

import com.sportygroup.jackpot.model.Bet;
import com.sportygroup.jackpot.model.JackpotReward;
import com.sportygroup.jackpot.service.JackpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Service responsible for simulating a Kafka Consumer that processes bets.
 * This class encapsulates the logic for jackpot contribution and reward evaluation,
 * acting as the listener for the "jackpot-bets" topic in this mocked setup.
 */
@Service
public class KafkaConsumerService {

    private final JackpotService jackpotService;

    /**
     * Constructor for dependency injection.
     * @param jackpotService The jackpot service.
     */
    @Autowired
    public KafkaConsumerService(JackpotService jackpotService) {
        this.jackpotService = jackpotService;
    }

    /**
     * Simulates consuming a bet message from Kafka.
     * This method orchestrates the contribution of the bet to the jackpot
     * and the evaluation of the bet for a jackpot reward.
     *
     * @param bet The bet consumed from the "Kafka topic".
     * @return A Mono that completes when the bet processing is done.
     */
    public Mono<Void> consumeBet(Bet bet) {
        System.out.println("KafkaConsumerService: Consuming bet " + bet.getBetId() + " for processing.");

        return jackpotService.contributeToJackpot(bet)
                .doOnSuccess(v -> System.out.println("KafkaConsumerService: Bet " + bet.getBetId() + " contributed to jackpot."))
                .then(Mono.defer(() -> jackpotService.evaluateReward(bet)))
                .doOnSuccess(optionalReward -> {
                    if (optionalReward.isPresent()) {
                        JackpotReward reward = optionalReward.get();
                        System.out.println("KafkaConsumerService: Bet " + bet.getBetId() + " won jackpot reward: " + reward.getJackpotRewardAmount());
                    } else {
                        System.out.println("KafkaConsumerService: Bet " + bet.getBetId() + " did not win a jackpot reward.");
                    }
                })
                .doOnError(e -> System.err.println("KafkaConsumerService: Error processing bet " + bet.getBetId() + ": " + e.getMessage()))
                .then();
    }
}