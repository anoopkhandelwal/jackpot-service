package com.sportygroup.jackpot.service.reward;

import com.sportygroup.jackpot.model.JackpotConfig;

import java.math.BigDecimal;

/**
 * Interface defining the contract for jackpot reward evaluation strategies.
 * This is a core part of the Strategy Pattern, enabling Open/Closed Principle.
 * New reward rules can be added by implementing this interface without
 * modifying existing JackpotService logic.
 *
 * The `checkWin` method here is synchronous as the calculation itself
 * is CPU-bound (mathematical operation and random number generation) and typically very fast.
 */
public interface RewardStrategy {
    /**
     * Checks if a bet wins a jackpot reward based on the strategy's rules.
     *
     * @param betAmount The original amount of the bet (can be used for contextual logic).
     * @param currentJackpotPool The current total amount in the jackpot pool (often influences chance).
     * @param config The specific configuration for this jackpot.
     * @return True if the bet wins the jackpot, false otherwise.
     */
    boolean checkWin(BigDecimal betAmount, BigDecimal currentJackpotPool, JackpotConfig config);
}