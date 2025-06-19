package com.sportygroup.jackpot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a user's bet in the system.
 * This is a simple POJO (Plain Old Java Object) for data transfer and storage.
 */
public class Bet {

    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal betAmount;
    private LocalDateTime createdAt;

    public Bet(String betId, String userId, String jackpotId, BigDecimal betAmount, LocalDateTime createdAt) {
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.betAmount = betAmount;
        this.createdAt = createdAt;
    }

    public Bet() {
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

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
