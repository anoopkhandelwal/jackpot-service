package com.sportygroup.jackpot.api.controller.request;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) for receiving bet publication requests.
 * Used by the BetController to capture request body data.
 */

public class BetRequest {
    private final String userId;
    private final String jackpotId;
    private final BigDecimal betAmount;

    public BetRequest(String userId, String jackpotId, BigDecimal betAmount) {
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.betAmount = betAmount;
    }

    public String getUserId() {
        return userId;
    }

    public String getJackpotId() {
        return jackpotId;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }



}