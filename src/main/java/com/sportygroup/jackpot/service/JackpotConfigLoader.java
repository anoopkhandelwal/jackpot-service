package com.sportygroup.jackpot.service;

import com.sportygroup.jackpot.model.JackpotConfig;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A component responsible for loading and providing JackpotConfig instances.
 * In a real application, this might load configurations from a database,
 * external file, or a configuration service. For this assignment, it's an in-memory map.
 *
 * This class primarily deals with synchronous Map operations, so no direct reactive
 * transformation is strictly necessary within its own methods, but its usage
 * in reactive services will be handled by wrapping its calls in Mono.fromCallable.
 */
@Component
public class JackpotConfigLoader {

    private final Map<String, JackpotConfig> jackpotConfigs = new ConcurrentHashMap<>();

    /**
     * Adds a jackpot configuration to the loader.
     * @param jackpotId The ID of the jackpot associated with this configuration.
     * @param config The JackpotConfig object.
     */
    public void addJackpotConfig(String jackpotId, JackpotConfig config) {
        jackpotConfigs.put(jackpotId, config);
        System.out.println("JackpotConfigLoader: Added config for Jackpot ID: " + jackpotId);
    }

    /**
     * Retrieves a jackpot configuration by its ID.
     * @param jackpotId The ID of the jackpot.
     * @return The JackpotConfig associated with the given ID, or null if not found.
     */
    public JackpotConfig getJackpotConfig(String jackpotId) {
        return jackpotConfigs.get(jackpotId);
    }
}