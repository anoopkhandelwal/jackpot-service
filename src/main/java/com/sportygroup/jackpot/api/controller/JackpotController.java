package com.sportygroup.jackpot.api.controller;

import com.sportygroup.jackpot.api.controller.response.JackpotRewardResponse;
import com.sportygroup.jackpot.model.JackpotReward;
import com.sportygroup.jackpot.repository.BetRepository;
import com.sportygroup.jackpot.service.JackpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST Controller for handling jackpot reward evaluation requests.
 * This acts as the API endpoint to evaluate if a bet wins a jackpot reward.
 * Endpoints now return Mono<ResponseEntity<T>> for non-blocking operations.
 */
@RestController
@RequestMapping("/api/jackpots")
public class JackpotController {

    private final JackpotService jackpotService;
    private final BetRepository betRepository;

    /**
     * Constructor for dependency injection.
     * @param jackpotService The jackpot service.
     * @param betRepository The bet repository.
     */
    @Autowired
    public JackpotController(JackpotService jackpotService, BetRepository betRepository) {
        this.jackpotService = jackpotService;
        this.betRepository = betRepository;
    }

    /**
     * API endpoint to evaluate if a bet wins a jackpot reward.
     *
     * This method first attempts to retrieve the full Bet object from the repository using the provided betId.
     * If the bet is not found (meaning the betId does not exist in the system),
     * it now returns a 404 Not Found response with an empty body, adhering to a stricter API contract.
     * The evaluation proceeds only if the Bet is found in the repository.
     *
     * @param betId The ID of the bet to evaluate for reward, passed as a query parameter.
     * @return A Mono emitting a ResponseEntity with JackpotRewardResponse indicating if a reward was won,
     * or a 404 Not Found with an empty body if the betId does not exist.
     */
    @GetMapping("/evaluate-reward")
    public Mono<ResponseEntity<JackpotRewardResponse>> evaluateReward(@RequestParam String betId) {
        if (betId == null || betId.isBlank()) {
            return Mono.just(ResponseEntity.badRequest().body(null));
        }
        return betRepository.findById(betId)
                .flatMap(betToEvaluate -> {
                    return jackpotService.evaluateReward(betToEvaluate)
                            .map(optionalReward -> {
                                if (optionalReward.isPresent()) {
                                    JackpotReward reward = optionalReward.get();
                                    return ResponseEntity.ok(new JackpotRewardResponse(
                                            reward.getBetId(),
                                            reward.getUserId(),
                                            reward.getJackpotId(),
                                            true,
                                            reward.getJackpotRewardAmount(),
                                            "Congratulations! You won the jackpot!"
                                    ));
                                } else {
                                    return ResponseEntity.ok(new JackpotRewardResponse(
                                            betToEvaluate.getBetId(),
                                            betToEvaluate.getUserId(),
                                            betToEvaluate.getJackpotId(),
                                            false,
                                            null,
                                            "Sorry, this bet did not win the jackpot."
                                    ));
                                }
                            })
                            .onErrorResume(IllegalArgumentException.class, e -> {
                                System.err.println("Error evaluating reward due to configuration issue: " + e.getMessage());
                                return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JackpotRewardResponse(
                                        betToEvaluate.getBetId(), betToEvaluate.getUserId(), betToEvaluate.getJackpotId(),
                                        false, null, "Error: Invalid jackpot configuration. " + e.getMessage()
                                )));
                            })
                            .onErrorResume(Exception.class, e -> {
                                System.err.println("Error evaluating reward for bet " + betToEvaluate.getBetId() + ": " + e.getMessage());
                                e.printStackTrace();
                                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JackpotRewardResponse(
                                        betToEvaluate.getBetId(), betToEvaluate.getUserId(), betToEvaluate.getJackpotId(),
                                        false, null, "An unexpected error occurred during evaluation."
                                )));
                            });
                })
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.println("JackpotController: Bet with ID '" + betId + "' not found in repository. Returning 404 Not Found with empty body.");
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
                }));
    }

}