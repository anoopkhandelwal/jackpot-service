package com.sportygroup.jackpot.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a record of a bet contributing to a jackpot.
 * This entity is used for historical tracking of contributions.
 */

public class JackpotContribution {

    private String betId;
    private String userId;
    private String jackpotId;
    private BigDecimal stakeAmount;
    private BigDecimal contributionAmount;
    private BigDecimal currentJackpotAmountAfterContribution;
    private LocalDateTime createdAt;

    public JackpotContribution(String betId, String userId, String jackpotId, BigDecimal stakeAmount,
                               BigDecimal contributionAmount, BigDecimal currentJackpotAmountAfterContribution, LocalDateTime createdAt) {
        this.betId = betId;
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.stakeAmount = stakeAmount;
        this.contributionAmount = contributionAmount;
        this.currentJackpotAmountAfterContribution = currentJackpotAmountAfterContribution;
        this.createdAt = createdAt;
    }

    public JackpotContribution() {
    }

    public String getBetId() {
        return betId;
    }

    public void setBetId(String betId) {
        this.betId = betId;
    }


    public String getJackpotId() {
        return jackpotId;
    }

    public void setJackpotId(String jackpotId) {
        this.jackpotId = jackpotId;
    }

    public BigDecimal getContributionAmount() {
        return contributionAmount;
    }

}