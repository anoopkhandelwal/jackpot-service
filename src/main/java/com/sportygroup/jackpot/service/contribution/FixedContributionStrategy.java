package com.sportygroup.jackpot.service.contribution;

import com.sportygroup.jackpot.model.JackpotConfig;
import com.sportygroup.jackpot.model.enums.ContributionStrategyType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Implements a fixed percentage contribution strategy.
 * The contribution is a fixed percentage of the bet amount, as defined in JackpotConfig.
 */
@Service(ContributionStrategyType.FIXED_CONTRIBUTION_STRATEGY_SERVICE_NAME)
public class FixedContributionStrategy implements ContributionStrategy {

    /**
     * Calculates the contribution based on a fixed percentage of the bet amount.
     * The percentage is retrieved from the `config.contributionFixedPercentage`.
     *
     * @param betAmount The original amount of the bet.
     * @param currentJackpotPool The current total amount in the jackpot pool (not used in this strategy).
     * @param config The specific configuration for this jackpot.
     * @return The calculated fixed contribution amount.
     * @throws IllegalArgumentException if `contributionFixedPercentage` is not set or invalid.
     */
    @Override
    public BigDecimal calculateContribution(BigDecimal betAmount, BigDecimal currentJackpotPool, JackpotConfig config) {
        // Validate configuration
        if (config == null || config.getContributionFixedPercentage() == 0) {
            throw new IllegalArgumentException("FixedContributionStrategy requires a valid fixed percentage in config.");
        }

        BigDecimal percentage = BigDecimal.valueOf(config.getContributionFixedPercentage());

        BigDecimal contribution = betAmount.multiply(percentage).setScale(2, RoundingMode.HALF_UP);

        System.out.println("FixedContributionStrategy: Bet " + betAmount + " -> Contributed " + contribution +
                " (fixed " + (config.getContributionFixedPercentage() * 100) + "%)");
        return contribution;
    }
}