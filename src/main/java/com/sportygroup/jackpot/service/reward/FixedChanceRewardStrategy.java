package com.sportygroup.jackpot.service.reward;

import com.sportygroup.jackpot.model.JackpotConfig;
import com.sportygroup.jackpot.model.enums.RewardStrategyType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Implements a fixed chance reward strategy.
 * The chance of winning is a constant percentage, regardless of bet amount or jackpot size.
 */
@Service(RewardStrategyType.FIXED_CHANCE_REWARD_STRATEGY_SERVICE_NAME)
public class FixedChanceRewardStrategy implements RewardStrategy {

    private final Random random = new Random();

    /**
     * Checks if a bet wins based on a fixed percentage chance.
     * The percentage is retrieved from `config.rewardFixedChancePercentage`.
     *
     * @param betAmount The original amount of the bet (not used in this strategy).
     * @param currentJackpotPool The current total amount in the jackpot pool (not used in this strategy).
     * @param config The specific configuration for this jackpot.
     * @return True if a randomly generated number falls within the winning chance, false otherwise.
     * @throws IllegalArgumentException if `rewardFixedChancePercentage` is not set or invalid.
     */
    @Override
    public boolean checkWin(BigDecimal betAmount, BigDecimal currentJackpotPool, JackpotConfig config) {

        if (config == null || config.getRewardFixedChancePercentage() <= 0) {
            System.err.println("FixedChanceRewardStrategy: Invalid or missing rewardFixedChancePercentage in config.");
            return false;
        }

        double randomValue = random.nextDouble();

        double winChance = config.getRewardFixedChancePercentage();

        boolean wins = randomValue < winChance;

        System.out.println("FixedChanceRewardStrategy: Chance: " + (winChance * 100) + "% | Random: " + String.format("%.6f", randomValue) + " -> Win: " + wins);
        return wins;
    }
}