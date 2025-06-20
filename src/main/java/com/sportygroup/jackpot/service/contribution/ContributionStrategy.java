package com.sportygroup.jackpot.service.contribution;

import com.sportygroup.jackpot.model.JackpotConfig;

import java.math.BigDecimal;

/**
 * Interface defining the contract for jackpot contribution strategies.
 * This is a core part of the Strategy Pattern, enabling Open/Closed Principle.
 * New contribution rules can be added by implementing this interface without
 * modifying existing JackpotService logic.
 *
 * The `calculateContribution` method here is synchronous as the calculation itself
 * is CPU-bound (mathematical operation) and typically very fast.
 */
public interface ContributionStrategy {
    /**
     * Calculates the contribution amount from a bet to a jackpot.
     *
     * @param betAmount The original amount of the bet.
     * @param currentJackpotPool The current total amount in the jackpot pool.
     * @param config The specific configuration for this jackpot.
     * @return The calculated contribution amount.
     */
    BigDecimal calculateContribution(BigDecimal betAmount, BigDecimal currentJackpotPool, JackpotConfig config);
}