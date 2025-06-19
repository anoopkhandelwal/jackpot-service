package com.sportygroup.jackpot.model.enums;

/**
 * Enum representing the types of contribution strategies available.
 * Each enum value provides a method to get its corresponding Spring bean name,
 * ensuring type safety and consistency when looking up strategy implementations.
 */
public enum ContributionStrategyType {

    FIXED_CONTRIBUTION_STRATEGY("fixedContributionStrategy"),
    VARIABLE_CONTRIBUTION_STRATEGY("variableContributionStrategy"),
    DEFAULT_CONTRIBUTION_STRATEGY("defaultContributionStrategy");

    public static final String FIXED_CONTRIBUTION_STRATEGY_SERVICE_NAME = "fixedContributionStrategy";
    public static final String VARIABLE_CONTRIBUTION_STRATEGY_SERVICE_NAME = "variableContributionStrategy";
    public static final String DEFAULT_CONTRIBUTION_STRATEGY_SERVICE_NAME = "defaultContributionStrategy";

    private final String value;


    ContributionStrategyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}