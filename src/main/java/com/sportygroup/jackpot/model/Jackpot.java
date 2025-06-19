package com.sportygroup.jackpot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a Jackpot in the system.
 * A jackpot has an ID, a current pool amount, an initial pool value (for reset),
 * and a configuration that dictates how contributions and rewards are handled.
 */
public class Jackpot {

    private String jackpotId;
    private BigDecimal currentPoolAmount;
    private BigDecimal initialPoolValue;
    private JackpotConfig config;
    private LocalDateTime createdAt;

    public Jackpot(String jackpotId, BigDecimal currentPoolAmount, BigDecimal initialPoolValue, JackpotConfig config, LocalDateTime createdAt) {
        this.jackpotId = jackpotId;
        this.currentPoolAmount = currentPoolAmount;
        this.initialPoolValue = initialPoolValue;
        this.config = config;
        this.createdAt = createdAt;
    }

    public Jackpot() {
    }

    public String getJackpotId() {
        return jackpotId;
    }

    public void setJackpotId(String jackpotId) {
        this.jackpotId = jackpotId;
    }

    public BigDecimal getCurrentPoolAmount() {
        return currentPoolAmount;
    }

    public BigDecimal getInitialPoolValue() {
        return initialPoolValue;
    }

    public JackpotConfig getConfig() {
        return config;
    }

    public void setConfig(JackpotConfig config) {
        this.config = config;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}