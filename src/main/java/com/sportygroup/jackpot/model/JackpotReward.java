package com.sportygroup.jackpot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a record of a bet winning a jackpot reward.
 * This entity is used for historical tracking of rewards.
 */
public class JackpotReward {

    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal jackpotRewardAmount;
    private LocalDateTime createdAt;

    public JackpotReward(String betId, String userId, String jackpotId, BigDecimal jackpotRewardAmount, LocalDateTime createdAt) {
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.jackpotRewardAmount = jackpotRewardAmount;
        this.createdAt = createdAt;
    }

    public JackpotReward() {
    }

    public String getBetId() {
        return betId;
    }

    public void setBetId(String betId) {
        this.betId = betId;
    }

    public String getUserId() {
        return userId;
    }

    public String getJackpotId() {
        return jackpotId;
    }

    public void setJackpotId(String jackpotId) {
        this.jackpotId = jackpotId;
    }

    public BigDecimal getJackpotRewardAmount() {
        return jackpotRewardAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}