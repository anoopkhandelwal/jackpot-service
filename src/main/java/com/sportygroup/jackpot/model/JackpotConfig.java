package com.sportygroup.jackpot.model;

import java.math.BigDecimal;
import com.sportygroup.jackpot.model.enums.ContributionStrategyType;
import com.sportygroup.jackpot.model.enums.RewardStrategyType;

/**
 * Configuration details for a specific Jackpot.
 * This class holds the names of the contribution and reward strategies to be used,
 * along with their specific parameters. This allows for dynamic strategy selection
 */
public class JackpotConfig {

    private ContributionStrategyType contributionStrategyType;
    private RewardStrategyType rewardStrategyType;
    private double contributionVariableInitialPercentage;
    private double contributionVariableDecreaseRate;
    private double rewardFixedChancePercentage;
    private double rewardVariableInitialChance;
    private double rewardVariableIncreaseRate;
    private BigDecimal rewardVariableChanceLimit;

    public JackpotConfig(final ContributionStrategyType contributionStrategyType, final RewardStrategyType rewardStrategyType,
                         double contributionFixedPercentage,
                         double contributionVariableInitialPercentage, double contributionVariableDecreaseRate,
                         double rewardFixedChancePercentage, double rewardVariableInitialChance,
                         double rewardVariableIncreaseRate, BigDecimal rewardVariableChanceLimit) {
        this.contributionStrategyType = contributionStrategyType;
        this.rewardStrategyType = rewardStrategyType;
        this.contributionFixedPercentage = contributionFixedPercentage;
        this.contributionVariableInitialPercentage = contributionVariableInitialPercentage;
        this.contributionVariableDecreaseRate = contributionVariableDecreaseRate;
        this.rewardFixedChancePercentage = rewardFixedChancePercentage;
        this.rewardVariableInitialChance = rewardVariableInitialChance;
        this.rewardVariableIncreaseRate = rewardVariableIncreaseRate;
        this.rewardVariableChanceLimit = rewardVariableChanceLimit;
    }

    private double contributionFixedPercentage;

    public JackpotConfig() {
    }

    public ContributionStrategyType getContributionStrategyType() {
        return contributionStrategyType;
    }

    public RewardStrategyType getRewardStrategyType() {
        return rewardStrategyType;
    }

    public double getContributionFixedPercentage() {
        return contributionFixedPercentage;
    }

    public double getContributionVariableInitialPercentage() {
        return contributionVariableInitialPercentage;
    }

    public double getContributionVariableDecreaseRate() {
        return contributionVariableDecreaseRate;
    }

    public double getRewardFixedChancePercentage() {
        return rewardFixedChancePercentage;
    }

    public double getRewardVariableInitialChance() {
        return rewardVariableInitialChance;
    }

    public double getRewardVariableIncreaseRate() {
        return rewardVariableIncreaseRate;
    }

    public BigDecimal getRewardVariableChanceLimit() {
        return rewardVariableChanceLimit;
    }


}