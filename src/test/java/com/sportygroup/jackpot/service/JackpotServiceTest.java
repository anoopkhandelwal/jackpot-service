package com.sportygroup.jackpot.service;

import com.sportygroup.jackpot.model.Jackpot;
import com.sportygroup.jackpot.model.JackpotConfig;
import com.sportygroup.jackpot.model.enums.ContributionStrategyType;
import com.sportygroup.jackpot.model.enums.RewardStrategyType;
import com.sportygroup.jackpot.repository.JackpotContributionRepository;
import com.sportygroup.jackpot.repository.JackpotRepository;
import com.sportygroup.jackpot.repository.JackpotRewardRepository;
import com.sportygroup.jackpot.service.contribution.ContributionStrategy;
import com.sportygroup.jackpot.service.reward.RewardStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for JackpotService using Reactor's StepVerifier.
 */
public class JackpotServiceTest {

    @Mock
    private JackpotRepository jackpotRepository;
    @Mock
    private JackpotContributionRepository jackpotContributionRepository;
    @Mock
    private JackpotRewardRepository jackpotRewardRepository;
    @Mock
    private JackpotConfigLoader jackpotConfigLoader;
    @Mock
    private ContributionStrategy fixedContributionStrategy;
    @Mock
    private ContributionStrategy variableContributionStrategy;
    @Mock
    private RewardStrategy fixedChanceRewardStrategy;
    @Mock
    private RewardStrategy variableChanceRewardStrategy;

    private JackpotService jackpotService;

    private Map<String, ContributionStrategy> contributionStrategies;
    private Map<String, RewardStrategy> rewardStrategies;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Mockito.reset(jackpotRepository, jackpotContributionRepository, jackpotRewardRepository, jackpotConfigLoader,
                fixedContributionStrategy, variableContributionStrategy, fixedChanceRewardStrategy, variableChanceRewardStrategy);

        this.contributionStrategies = new HashMap<>();
        this.contributionStrategies.put(ContributionStrategyType.FIXED_CONTRIBUTION_STRATEGY.getValue(), fixedContributionStrategy);
        this.contributionStrategies.put(ContributionStrategyType.VARIABLE_CONTRIBUTION_STRATEGY.getValue(), variableContributionStrategy);

        this.rewardStrategies = new HashMap<>();
        this.rewardStrategies.put(RewardStrategyType.FIXED_CHANCE_REWARD_STRATEGY.getValue(), fixedChanceRewardStrategy);
        this.rewardStrategies.put(RewardStrategyType.VARIABLE_CHANCE_REWARD_STRATEGY.getValue(), variableChanceRewardStrategy);

        jackpotService = new JackpotService(
                jackpotRepository,
                jackpotContributionRepository,
                jackpotRewardRepository,
                this.contributionStrategies,
                this.rewardStrategies
        );

        when(jackpotRepository.save(any(Jackpot.class))).thenReturn(Mono.just(new Jackpot("mock-jp", BigDecimal.ZERO, BigDecimal.ZERO,
                new JackpotConfig(), LocalDateTime.now())));
    }

    @Test
    void testContributeToJackpotSuccessFixed() {
    }

    @Test
    void testContributeToJackpotNotFound() {
    }

    @Test
    void testContributeToJackpotMissingConfig() {
    }


    @Test
    void testEvaluateRewardWinFixedChance() {
    }

    @Test
    void testEvaluateRewardLossFixedChance() {
    }

    @Test
    void testEvaluateRewardJackpotNotFound() {
    }

    @Test
    void testEvaluateRewardMissingConfig() {
    }

    @Test
    void testEvaluateRewardStrategyNotFound() {

    }
}
