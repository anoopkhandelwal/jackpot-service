package com.sportygroup.jackpot.model.enums;

/**
 * Enum representing the types of reward strategies available.
 * Each enum value provides a method to get its corresponding Spring bean name,
 * ensuring type safety and consistency when looking up strategy implementations.
 */
public enum RewardStrategyType {

    FIXED_CHANCE_REWARD_STRATEGY("fixedChanceRewardStrategy"),
    VARIABLE_CHANCE_REWARD_STRATEGY("variableChanceRewardStrategy"),
    DEFAULT_CHANCE_REWARD_STRATEGY("defaultChanceRewardStrategy");


    public static final String FIXED_CHANCE_REWARD_STRATEGY_SERVICE_NAME = "fixedChanceRewardStrategy";
    public static final String VARIABLE_CHANCE_REWARD_STRATEGY_SERVICE_NAME = "variableChanceRewardStrategy";
    public static final String DEFAULT_CHANCE_REWARD_STRATEGY_SERVICE_NAME = "defaultChanceRewardStrategy";

    private final String value;

    RewardStrategyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
