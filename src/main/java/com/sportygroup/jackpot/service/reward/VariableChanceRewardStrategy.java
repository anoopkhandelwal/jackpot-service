package com.sportygroup.jackpot.service.reward;

import com.sportygroup.jackpot.model.JackpotConfig;
import com.sportygroup.jackpot.model.enums.RewardStrategyType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * Implements a variable chance reward strategy.
 * The chance for reward starts smaller and over time it becomes bigger as the jackpot pool increases.
 * If the jackpot pool hits a limit, then the chance becomes 100%.
 */
@Service(RewardStrategyType.VARIABLE_CHANCE_REWARD_STRATEGY_SERVICE_NAME)
public class VariableChanceRewardStrategy implements RewardStrategy {

    private final Random random = new Random();

    private static final BigDecimal POOL_SCALING_FACTOR = BigDecimal.valueOf(1000.0);

    /**
     * Checks if a bet wins based on a variable chance that increases with the jackpot pool.
     * If the `currentJackpotPool` reaches `config.rewardVariableChanceLimit`, the chance becomes 100%.
     *
     * @param betAmount The original amount of the bet (not used in this strategy).
     * @param currentJackpotPool The current total amount in the jackpot pool.
     * @param config The specific configuration for this jackpot.
     * @return True if a randomly generated number falls within the winning chance, false otherwise.
     * @throws IllegalArgumentException if required config parameters are not set or invalid.
     */
    @Override
    public boolean checkWin(BigDecimal betAmount, BigDecimal currentJackpotPool, JackpotConfig config) {

        if (config == null || config.getRewardVariableInitialChance() <= 0 || config.getRewardVariableIncreaseRate() <= 0 || config.getRewardVariableChanceLimit() == null) {
            System.err.println("VariableChanceRewardStrategy: Invalid or missing required config parameters.");
            return false;
        }

        double initialChance = config.getRewardVariableInitialChance();
        double increaseRate = config.getRewardVariableIncreaseRate();
        BigDecimal chanceLimit = config.getRewardVariableChanceLimit();

        if (currentJackpotPool.compareTo(chanceLimit) >= 0) {
            System.out.println("VariableChanceRewardStrategy: Jackpot pool (" + currentJackpotPool + ") hit limit (" + chanceLimit + "). Chance is 100%.");
            return true;
        }

        BigDecimal increaseFactor = currentJackpotPool
                .divide(POOL_SCALING_FACTOR, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(increaseRate));

        double effectiveChance = BigDecimal.valueOf(initialChance)
                .add(increaseFactor)
                .min(BigDecimal.ONE) // Max chance is 1.0 (100%)
                .doubleValue();

        double randomValue = random.nextDouble();

        boolean wins = randomValue < effectiveChance;

        System.out.println("VariableChanceRewardStrategy: Pool: " + currentJackpotPool +
                " | Effective Chance: " + (effectiveChance * 100) + "% | Random: " + String.format("%.6f", randomValue) + " -> Win: " + wins);
        return wins;
    }
}