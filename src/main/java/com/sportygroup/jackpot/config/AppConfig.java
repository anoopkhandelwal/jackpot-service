package com.sportygroup.jackpot.config;

import com.sportygroup.jackpot.consumer.KafkaConsumerService;
import com.sportygroup.jackpot.repository.BetRepository;
import com.sportygroup.jackpot.repository.InMemBetRepository;
import com.sportygroup.jackpot.repository.InMemJackpotContributionRepository;
import com.sportygroup.jackpot.repository.InMemJackpotRepository;
import com.sportygroup.jackpot.repository.InMemJackpotRewardRepository;
import com.sportygroup.jackpot.repository.JackpotContributionRepository;
import com.sportygroup.jackpot.repository.JackpotRepository;
import com.sportygroup.jackpot.repository.JackpotRewardRepository;
import com.sportygroup.jackpot.producer.KafkaProducerService;
import com.sportygroup.jackpot.producer.MockKafkaProducerService;
import com.sportygroup.jackpot.service.BetService;
import com.sportygroup.jackpot.service.JackpotConfigLoader;
import com.sportygroup.jackpot.service.JackpotService;
import com.sportygroup.jackpot.service.contribution.ContributionStrategy;
import com.sportygroup.jackpot.service.contribution.FixedContributionStrategy;
import com.sportygroup.jackpot.service.contribution.VariableContributionStrategy;
import com.sportygroup.jackpot.service.reward.FixedChanceRewardStrategy;
import com.sportygroup.jackpot.service.reward.RewardStrategy;
import com.sportygroup.jackpot.service.reward.VariableChanceRewardStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Spring configuration class for defining application beans.
 * This centralizes the instantiation and wiring of dependencies, adhering to DIP.
 * All repository and service beans are now configured to work with reactive types.
 */
@Configuration
public class AppConfig {

    /**
     * Defines the in-memory BetRepository bean.
     * @return An instance of InMemBetRepository.
     */
    @Bean
    public BetRepository betRepository() {
        return new InMemBetRepository();
    }

    /**
     * Defines the in-memory JackpotRepository bean.
     * @return An instance of InMemJackpotRepository.
     */
    @Bean
    public JackpotRepository jackpotRepository() {
        return new InMemJackpotRepository();
    }

    /**
     * Defines the in-memory JackpotContributionRepository bean.
     * @return An instance of InMemJackpotContributionRepository.
     */
    @Bean
    public JackpotContributionRepository jackpotContributionRepository() {
        return new InMemJackpotContributionRepository();
    }

    /**
     * Defines the in-memory JackpotRewardRepository bean.
     * @return An instance of InMemJackpotRewardRepository.
     */
    @Bean
    public JackpotRewardRepository jackpotRewardRepository() {
        return new InMemJackpotRewardRepository();
    }

    /**
     * Defines the mock KafkaConsumerService bean.
     * @return An instance of KafkaConsumerService.
     */
    @Bean
    public KafkaConsumerService kafkaConsumerService(JackpotService jackpotService) {
        return new KafkaConsumerService(jackpotService);
    }

    /**
     * Defines the mock KafkaProducerService bean.
     * @return An instance of MockKafkaProducerService.
     */
    @Bean
    public KafkaProducerService kafkaProducerService(KafkaConsumerService kafkaConsumerService) {
        return new MockKafkaProducerService(kafkaConsumerService);
    }

    /**
     * Defines the JackpotConfigLoader bean.
     * @return An instance of JackpotConfigLoader.
     */
    @Bean
    public JackpotConfigLoader jackpotConfigLoader() {
        return new JackpotConfigLoader();
    }

    // --- Contribution Strategies ---

    /**
     * Defines the FixedContributionStrategy bean.
     * The bean name "fixedContributionStrategy" is used for lookup in JackpotService.
     * @return An instance of FixedContributionStrategy.
     */
    @Bean
    public FixedContributionStrategy fixedContributionStrategy() {
        return new FixedContributionStrategy();
    }

    /**
     * Defines the VariableContributionStrategy bean.
     * The bean name "variableContributionStrategy" is used for lookup in JackpotService.
     * @return An instance of VariableContributionStrategy.
     */
    @Bean
    public VariableContributionStrategy variableContributionStrategy() {
        return new VariableContributionStrategy();
    }

    /**
     * Collects all ContributionStrategy beans into a Map.
     * Spring automatically injects all beans of type `ContributionStrategy` into this map,
     * using their bean names as keys. This is crucial for dynamic strategy selection (DIP).
     * @return A map of contribution strategies.
     */
    @Bean
    public Map<String, ContributionStrategy> contributionStrategies(
            FixedContributionStrategy fixedContributionStrategy,
            VariableContributionStrategy variableContributionStrategy) {
        Map<String, ContributionStrategy> strategies = new HashMap<>();
        strategies.put("fixedContributionStrategy", fixedContributionStrategy);
        strategies.put("variableContributionStrategy", variableContributionStrategy);
        return strategies;
    }

    // --- Reward Strategies ---

    /**
     * Defines the FixedChanceRewardStrategy bean.
     * The bean name "fixedChanceRewardStrategy" is used for lookup in JackpotService.
     * @return An instance of FixedChanceRewardStrategy.
     */
    @Bean
    public FixedChanceRewardStrategy fixedChanceRewardStrategy() {
        return new FixedChanceRewardStrategy();
    }

    /**
     * Defines the VariableChanceRewardStrategy bean.
     * The bean name "variableChanceRewardStrategy" is used for lookup in JackpotService.
     * @return An instance of VariableChanceRewardStrategy.
     */
    @Bean
    public VariableChanceRewardStrategy variableChanceRewardStrategy() {
        return new VariableChanceRewardStrategy();
    }

    /**
     * Collects all RewardStrategy beans into a Map.
     * Spring automatically injects all beans of type `RewardStrategy` into this map,
     * using their bean names as keys. This is crucial for dynamic strategy selection (DIP).
     * @return A map of reward strategies.
     */
    @Bean
    public Map<String, RewardStrategy> rewardStrategies(
            FixedChanceRewardStrategy fixedChanceRewardStrategy,
            VariableChanceRewardStrategy variableChanceRewardStrategy) {
        Map<String, RewardStrategy> strategies = new HashMap<>();
        strategies.put("fixedChanceRewardStrategy", fixedChanceRewardStrategy);
        strategies.put("variableChanceRewardStrategy", variableChanceRewardStrategy);
        return strategies;
    }

    // --- Services ---

    /**
     * Defines the BetService bean.
     * @param betRepository The injected BetRepository.
     * @param kafkaProducerService The injected KafkaProducerService.
     * @return An instance of BetService.
     */
    @Bean
    public BetService betService(BetRepository betRepository, KafkaProducerService kafkaProducerService) {
        return new BetService(betRepository, kafkaProducerService);
    }

    /**
     * Defines the JackpotService bean.
     * @param jackpotRepository The injected JackpotRepository.
     * @param jackpotContributionRepository The injected JackpotContributionRepository.
     * @param jackpotRewardRepository The injected JackpotRewardRepository.
     * @param contributionStrategies The injected map of contribution strategies.
     * @param rewardStrategies The injected map of reward strategies.
     * @return An instance of JackpotService.
     */
    @Bean
    public JackpotService jackpotService(
            JackpotRepository jackpotRepository,
            JackpotContributionRepository jackpotContributionRepository,
            JackpotRewardRepository jackpotRewardRepository,
            Map<String, ContributionStrategy> contributionStrategies,
            Map<String, RewardStrategy> rewardStrategies) {
        return new JackpotService(
                jackpotRepository,
                jackpotContributionRepository,
                jackpotRewardRepository,
                contributionStrategies,
                rewardStrategies
        );
    }
}