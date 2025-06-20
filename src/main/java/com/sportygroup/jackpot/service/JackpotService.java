package com.sportygroup.jackpot.service;

import com.sportygroup.jackpot.model.Bet;
import com.sportygroup.jackpot.model.Jackpot;
import com.sportygroup.jackpot.model.JackpotConfig;
import com.sportygroup.jackpot.model.JackpotContribution;
import com.sportygroup.jackpot.model.JackpotReward;
import com.sportygroup.jackpot.repository.JackpotContributionRepository;
import com.sportygroup.jackpot.repository.JackpotRepository;
import com.sportygroup.jackpot.repository.JackpotRewardRepository;
import com.sportygroup.jackpot.service.contribution.ContributionStrategy;
import com.sportygroup.jackpot.service.reward.RewardStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service responsible for managing jackpot contributions and rewards.
 * This class orchestrates the interaction between repositories and strategies.
 *
 * This version of JackpotService explicitly saves JackpotContribution and JackpotReward
 * records to their respective repositories.
 * Concurrency for jackpot updates is handled at the repository level (e.g., using `replace` in `InMemJackpotRepository`).
 */
@Service
public class JackpotService {

    private final JackpotRepository jackpotRepository;
    private final JackpotContributionRepository jackpotContributionRepository;
    private final JackpotRewardRepository jackpotRewardRepository;
    private final Map<String, ContributionStrategy> contributionStrategies;
    private final Map<String, RewardStrategy> rewardStrategies;

    /**
     * Constructor for dependency injection.
     * Spring will inject all beans that implement ContributionStrategy and RewardStrategy
     * into the respective maps, keyed by their bean names (which we'll configure in AppConfig).
     * @param jackpotRepository The repository for managing jackpots.
     * @param jackpotContributionRepository The repository for managing jackpot contribution records.
     * @param jackpotRewardRepository The repository for managing jackpot reward records.
     * @param contributionStrategies Map of all available contribution strategies.
     * @param rewardStrategies Map of all available reward strategies.
     */
    @Autowired
    public JackpotService(
            JackpotRepository jackpotRepository,
            JackpotContributionRepository jackpotContributionRepository,
            JackpotRewardRepository jackpotRewardRepository,
            Map<String, ContributionStrategy> contributionStrategies,
            Map<String, RewardStrategy> rewardStrategies) {
        this.jackpotRepository = jackpotRepository;
        this.jackpotContributionRepository = jackpotContributionRepository;
        this.jackpotRewardRepository = jackpotRewardRepository;
        this.contributionStrategies = new ConcurrentHashMap<>(contributionStrategies);
        this.rewardStrategies = new ConcurrentHashMap<>(rewardStrategies);
    }



    /**
     * Handles the contribution of a bet to its matching jackpot pool.
     * Uses the configured ContributionStrategy for the specific jackpot.
     * Concurrency is managed by the underlying `JackpotRepository.update` which
     * employs an optimistic locking approach for in-memory updates.
     *
     * @param bet The bet that is contributing.
     * @return A Mono that completes when the contribution is processed and recorded.
     */
    public Mono<Void> contributeToJackpot(Bet bet) {
        return jackpotRepository.findById(bet.getJackpotId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No matching jackpot found for ID: " + bet.getJackpotId())))
                .flatMap(jackpot -> {
                    JackpotConfig config = jackpot.getConfig();
                    if (config == null) {
                        return Mono.error(new IllegalStateException("Jackpot " + jackpot.getJackpotId() + " has no configuration."));
                    }

                    ContributionStrategy strategy = contributionStrategies.get(config.getContributionStrategyType().getValue());
                    if (strategy == null) {
                        return Mono.error(new IllegalArgumentException("No contribution strategy found for type: " + config.getContributionStrategyType()));
                    }

                    BigDecimal contributionAmount = strategy.calculateContribution(
                            bet.getBetAmount(),
                            jackpot.getCurrentPoolAmount(),
                            config
                    );

                    Jackpot updatedJackpot = new Jackpot(
                            jackpot.getJackpotId(),
                            jackpot.getCurrentPoolAmount().add(contributionAmount),
                            jackpot.getInitialPoolValue(),
                            jackpot.getConfig(),
                            jackpot.getCreatedAt()
                    );

                    return jackpotRepository.update(updatedJackpot)
                            .flatMap(savedJackpot -> {
                                JackpotContribution contributionRecord = new JackpotContribution(
                                        bet.getBetId(),
                                        bet.getUserId(),
                                        savedJackpot.getJackpotId(),
                                        bet.getBetAmount(),
                                        contributionAmount,
                                        savedJackpot.getCurrentPoolAmount(),
                                        LocalDateTime.now()
                                );
                                return jackpotContributionRepository.save(contributionRecord)
                                        .doOnSuccess(c -> System.out.println("JackpotService: Bet " + bet.getBetId() + " contributed " + contributionAmount +
                                                " to Jackpot " + savedJackpot.getJackpotId() + ". New pool: " + savedJackpot.getCurrentPoolAmount()))
                                        .then();
                            });
                })
                .doOnError(e -> System.err.println("JackpotService: Error during contribution for bet " + bet.getBetId() + ": " + e.getMessage()));
    }

    /**
     * Evaluates if a bet wins the jackpot reward.
     * Uses the configured RewardStrategy for the specific jackpot.
     * If a reward is won, the jackpot pool is reset.
     * Concurrency is managed by the underlying `JackpotRepository.update` via optimistic locking.
     *
     * @param bet The bet to evaluate for reward.
     * @return A Mono emitting the JackpotReward if won, or empty otherwise.
     */
    public Mono<Optional<JackpotReward>> evaluateReward(Bet bet) {
        return jackpotRepository.findById(bet.getJackpotId())
                .switchIfEmpty(Mono.error(new IllegalArgumentException("No matching jackpot found for ID: " + bet.getJackpotId())))
                .flatMap(jackpot -> {
                    JackpotConfig config = jackpot.getConfig();
                    if (config == null) {
                        return Mono.error(new IllegalStateException("Jackpot " + jackpot.getJackpotId() + " has no configuration."));
                    }

                    RewardStrategy strategy = rewardStrategies.get(config.getRewardStrategyType().getValue());
                    if (strategy == null) {
                        return Mono.error(new IllegalArgumentException("No reward strategy found for type: " + config.getRewardStrategyType()));
                    }

                    boolean wins = strategy.checkWin(
                            bet.getBetAmount(),
                            jackpot.getCurrentPoolAmount(),
                            config
                    );

                    if (wins) {
                        BigDecimal rewardAmount = jackpot.getCurrentPoolAmount();
                        System.out.println("JackpotService: Bet " + bet.getBetId() + " WON Jackpot " + jackpot.getJackpotId() + " with reward: " + rewardAmount);

                        Jackpot resetJackpot = new Jackpot(
                                jackpot.getJackpotId(),
                                jackpot.getInitialPoolValue(),
                                jackpot.getInitialPoolValue(),
                                jackpot.getConfig(),
                                jackpot.getCreatedAt()
                        );

                        return jackpotRepository.update(resetJackpot)
                                .flatMap(savedJackpot -> {
                                    System.out.println("JackpotService: Jackpot " + savedJackpot.getJackpotId() + " reset to initial pool: " + savedJackpot.getInitialPoolValue());
                                    JackpotReward rewardRecord = new JackpotReward(
                                            bet.getBetId(),
                                            bet.getUserId(),
                                            savedJackpot.getJackpotId(),
                                            rewardAmount,
                                            LocalDateTime.now()
                                    );
                                    return jackpotRewardRepository.save(rewardRecord)
                                            .map(Optional::of);
                                });
                    } else {
                        System.out.println("JackpotService: Bet " + bet.getBetId() + " did NOT win Jackpot " + jackpot.getJackpotId());
                        return Mono.just(Optional.<JackpotReward>empty());
                    }
                })
                .onErrorResume(IllegalArgumentException.class, e ->
                        Mono.error(new IllegalArgumentException(e.getMessage()))
                )
                .onErrorResume(IllegalStateException.class, e ->
                        Mono.error(new IllegalStateException(e.getMessage()))
                );
    }
}