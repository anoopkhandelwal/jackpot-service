package com.sportygroup.jackpot.api.controller;

import com.sportygroup.jackpot.model.Bet;
import com.sportygroup.jackpot.model.JackpotReward;
import com.sportygroup.jackpot.repository.BetRepository;
import com.sportygroup.jackpot.service.JackpotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Integration tests for JackpotController using WebTestClient.
 * Uses @WebFluxTest for slicing the Spring context to only reactive web components.
 */
@WebFluxTest(JackpotController.class)
public class JackpotControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private JackpotService jackpotService;

    @MockitoBean
    private BetRepository betRepository;

    private Bet sampleBet;
    private JackpotReward sampleReward;

    @BeforeEach
    void setUp() {
        sampleBet = new Bet("bet-456", "user-xyz", "JP-2", BigDecimal.valueOf(200.00), LocalDateTime.now());
        sampleReward = new JackpotReward("bet-456", "user-xyz", "JP-2", BigDecimal.valueOf(1500.00), LocalDateTime.now());
        Mockito.when(betRepository.findById("bet-456"))
                .thenReturn(Mono.just(sampleBet));
    }

    @Test
    void testEvaluateRewardWin() {
    }

    @Test
    void testEvaluateRewardLoss() {
    }

    @Test
    void testEvaluateRewardBadRequestInvalidInput() {
    }

    @Test
    void testEvaluateRewardBadRequestInvalidJackpotConfig() {
    }

    @Test
    void testEvaluateRewardInternalServerError() {
    }
}