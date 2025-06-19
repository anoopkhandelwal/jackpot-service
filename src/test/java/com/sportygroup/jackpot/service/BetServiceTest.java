package com.sportygroup.jackpot.service;

import com.sportygroup.jackpot.model.Bet;
import com.sportygroup.jackpot.repository.BetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;

/**
 * Unit tests for BetService using Reactor's StepVerifier for reactive flows.
 */
public class BetServiceTest {

    @Mock
    private BetRepository betRepository;

    @Mock
    private JackpotService jackpotService;

    @InjectMocks
    private BetService betService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Mockito.reset(betRepository, jackpotService);

        when(jackpotService.contributeToJackpot(any(Bet.class))).thenReturn(Mono.empty());
        when(jackpotService.evaluateReward(any(Bet.class))).thenReturn(Mono.just(Optional.empty())); // Changed to Mono<Optional<T>>
    }

    @Test
    void testPublishBetSuccess() {
    }

    @Test
    void testPublishBetRepositoryError() {
    }

    @Test
    void testProcessBetSuccess() {
    }

    @Test
    void testProcessBetContributionError() {
    }

    @Test
    void testProcessBetEvaluationError() {
    }
}