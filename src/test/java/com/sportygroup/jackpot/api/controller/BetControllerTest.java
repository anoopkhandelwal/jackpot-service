package com.sportygroup.jackpot.api.controller;

import com.sportygroup.jackpot.api.controller.request.BetRequest;
import com.sportygroup.jackpot.model.Bet;
import com.sportygroup.jackpot.producer.KafkaProducerService;
import com.sportygroup.jackpot.service.BetService;
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
 * Integration tests for BetController using WebTestClient.
 * Uses @WebFluxTest for slicing the Spring context to only reactive web components.
 */
@WebFluxTest(BetController.class)
public class BetControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private KafkaProducerService kafkaProducerService;

    @MockitoBean
    private BetService betService;

    private Bet sampleBet;

    private BetRequest sampleBetRequest;

    @BeforeEach
    void setUp() {
        sampleBet = new Bet("bet-123", "user-abc", "JP-1", BigDecimal.valueOf(100.00), LocalDateTime.now());
        sampleBetRequest = new BetRequest("user-abc", "JP-1", BigDecimal.valueOf(100.00));

        Mockito.when(kafkaProducerService.publishBet(Mockito.any(Bet.class)))
                .thenReturn(Mono.empty());

        Mockito.when(betService.publishBet(Mockito.any(Bet.class)))
                .thenReturn(Mono.just(sampleBet));
    }

    @Test
    void testPublishBetSuccess() {
    }

    @Test
    void testPublishBetBadRequest() {
    }

    @Test
    void testPublishBetInternalServerError() {
    }
}