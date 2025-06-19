package com.sportygroup.jackpot.api.controller.response;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) for sending jackpot reward evaluation responses.
 * Used by the JackpotController to return reward details.
 */
public class JackpotRewardResponse {

    private String betId;
    private String userId;
    private String jackpotId;
    private boolean won;
    private BigDecimal rewardAmount;
    private String message;

    public JackpotRewardResponse(String betId, String userId, String jackpotId, boolean won, BigDecimal rewardAmount, String message) {
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.won = won;
        this.rewardAmount = rewardAmount;
        this.message = message;
    }

    public String getBetId() {
        return betId;
    }

    public String getUserId() {
        return userId;
    }

    public String getJackpotId() {
        return jackpotId;
    }

    public boolean isWon() {
        return won;
    }

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public String getMessage() {
        return message;
    }
}