package com.sportygroup.jackpot.service.contribution;

import com.sportygroup.jackpot.model.JackpotConfig;
import com.sportygroup.jackpot.model.enums.ContributionStrategyType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implements a variable percentage contribution strategy.
 * The contribution percentage starts bigger and decreases at a fixed rate as the jackpot pool increases.
 */
@Service(ContributionStrategyType.VARIABLE_CONTRIBUTION_STRATEGY_SERVICE_NAME)
public class VariableContributionStrategy implements ContributionStrategy {

    // Define a scaling factor for the rate, to ensure the decrease is noticeable but not too drastic.
    private static final BigDecimal POOL_SCALING_FACTOR = BigDecimal.valueOf(1000.0); // Example: every 1000 units in pool affects the rate.

    /**
     * Calculates the contribution based on a variable percentage.
     * The percentage starts high and decreases as the `currentJackpotPool` increases.
     * The decrease rate is applied based on `config.contributionVariableDecreaseRate`.
     *
     * @param betAmount The original amount of the bet.
     * @param currentJackpotPool The current total amount in the jackpot pool.
     * @param config The specific configuration for this jackpot.
     * @return The calculated variable contribution amount.
     * @throws IllegalArgumentException if required config parameters are not set or invalid.
     */
    @Override
    public BigDecimal calculateContribution(BigDecimal betAmount, BigDecimal currentJackpotPool, JackpotConfig config) {

        if (config == null || config.getContributionVariableInitialPercentage() == 0 || config.getContributionVariableDecreaseRate() == 0) {
            throw new IllegalArgumentException("VariableContributionStrategy requires valid initial percentage and decrease rate in config.");
        }

        BigDecimal decreaseFactor = currentJackpotPool
                .divide(POOL_SCALING_FACTOR, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(config.getContributionVariableDecreaseRate()));

        BigDecimal effectivePercentage = BigDecimal.valueOf(config.getContributionVariableInitialPercentage())
                .subtract(decreaseFactor)
                .max(BigDecimal.valueOf(0.001));

        BigDecimal contribution = betAmount.multiply(effectivePercentage).setScale(2, RoundingMode.HALF_UP);

        System.out.println("VariableContributionStrategy: Bet " + betAmount + " | Pool " + currentJackpotPool +
                " -> Effective Percentage: " + effectivePercentage.multiply(BigDecimal.valueOf(100)).setScale(4, RoundingMode.HALF_UP) +
                "% | Contributed: " + contribution);

        return contribution;
    }
}