package com.sportygroup.jackpot.service.initializer;

import com.sportygroup.jackpot.model.Jackpot;
import com.sportygroup.jackpot.model.JackpotConfig;
import com.sportygroup.jackpot.model.enums.ContributionStrategyType;
import com.sportygroup.jackpot.model.enums.RewardStrategyType;
import com.sportygroup.jackpot.repository.JackpotRepository;
import com.sportygroup.jackpot.service.JackpotConfigLoader;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Component responsible for initializing default jackpot data into the system
 * during application startup. This class ensures that a set of predefined jackpots
 * are created and saved to the repository when the Spring application context
 * is ready.
 */
@Component
public class JackpotInitializer implements ApplicationRunner {

    private final JackpotRepository jackpotRepository;
    private final JackpotConfigLoader jackpotConfigLoader;


    public JackpotInitializer(JackpotRepository jackpotRepository, JackpotConfigLoader jackpotConfigLoader) {
        this.jackpotRepository = jackpotRepository;
        this.jackpotConfigLoader = jackpotConfigLoader;
    }

    @Override
    public void run(ApplicationArguments args) {
        initializeDefaultJackpots().block();
    }

    /**
     * Initializes default jackpots when the service starts.
     * This simulates pre-existing jackpots in a real system.
     * Uses `Mono.when` to ensure all initializations complete.
     */
    private Mono<Void> initializeDefaultJackpots() {
        System.out.println("JackpotService: Initializing default jackpots...");

        Mono<Void> fixedJackpot = Mono.fromCallable(() -> {
                    JackpotConfig config = new JackpotConfig(
                            ContributionStrategyType.FIXED_CONTRIBUTION_STRATEGY,
                            RewardStrategyType.FIXED_CHANCE_REWARD_STRATEGY,
                            0.01, // 1% fixed contribution
                            0, 0,
                            0.001, // 0.1% fixed chance reward
                            0, 0, BigDecimal.ZERO // N/A for variable chance reward
                    );
                    jackpotConfigLoader.addJackpotConfig("JP-1", config);
                    return new Jackpot("JP-1", BigDecimal.valueOf(1000.00), BigDecimal.valueOf(1000.00), config, LocalDateTime.now());
                }).flatMap(jackpotRepository::save)
                .doOnSuccess(j -> System.out.println("Jackpot JP-1 initialized: " + j.getJackpotId()))
                .then();

        Mono<Void> fixedJackpotWin = Mono.fromCallable(() -> {
                    JackpotConfig config = new JackpotConfig(
                            ContributionStrategyType.FIXED_CONTRIBUTION_STRATEGY,
                            RewardStrategyType.FIXED_CHANCE_REWARD_STRATEGY,
                            0.01, // 1% fixed contribution
                            0, 0,
                            100.0,
                            0, 0, BigDecimal.ZERO
                    );
                    jackpotConfigLoader.addJackpotConfig("JP-2", config);
                    return new Jackpot("JP-2", BigDecimal.valueOf(1000.00), BigDecimal.valueOf(1000.00), config, LocalDateTime.now());
                }).flatMap(jackpotRepository::save)
                .doOnSuccess(j -> System.out.println("Jackpot JP-2 initialized: " + j.getJackpotId()))
                .then();

        Mono<Void> variableJackpot = Mono.fromCallable(() -> {

                    JackpotConfig config = new JackpotConfig(
                            ContributionStrategyType.VARIABLE_CONTRIBUTION_STRATEGY,
                            RewardStrategyType.VARIABLE_CHANCE_REWARD_STRATEGY,
                            0, // N/A for fixed contribution
                            0.05, 0.0001, // 5% initial, decreases by 0.01% per thousand units increase in pool
                            0, // N/A for fixed chance reward
                            0.00001, 0.000001, BigDecimal.valueOf(100000.00) // 0.001% initial, increases by 0.0001% per thousand units pool, 100k limit
                    );
                    jackpotConfigLoader.addJackpotConfig("JP-3", config);
                    return new Jackpot("JP-3", BigDecimal.valueOf(500.00), BigDecimal.valueOf(500.00), config, LocalDateTime.now());
                }).flatMap(jackpotRepository::save)
                .doOnSuccess(j -> System.out.println("Jackpot JP-3 initialized: " + j.getJackpotId()))
                .then();

        Mono<Void> variableJackpotWin = Mono.fromCallable(() -> {
                    JackpotConfig config = new JackpotConfig(
                            ContributionStrategyType.VARIABLE_CONTRIBUTION_STRATEGY,
                            RewardStrategyType.VARIABLE_CHANCE_REWARD_STRATEGY,
                            0, // N/A for fixed contribution
                            0.05, 0.0001, // Example variable contribution (not affecting win guarantee)
                            0, // N/A for fixed chance reward
                            0.00001, // Small initial chance
                            1000.0,
                            BigDecimal.ONE
                    );
                    jackpotConfigLoader.addJackpotConfig("JP-4", config);
                    return new Jackpot("JP-4", BigDecimal.valueOf(500.00), BigDecimal.valueOf(500.00), config, LocalDateTime.now());
                }).flatMap(jackpotRepository::save)
                .doOnSuccess(j -> System.out.println("Jackpot JP-4 initialized: " + j.getJackpotId()))
                .then();


        return Mono.when(fixedJackpot,fixedJackpotWin, variableJackpot, variableJackpotWin)
                .doOnTerminate(() -> System.out.println("JackpotService: Default jackpots initialization completed."));
    }
}
