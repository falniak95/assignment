package com.furkanalniak.assignment.scheduler;

import com.furkanalniak.assignment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionScheduler {
    private final TransactionService transactionService;

    @Scheduled(fixedRate = 60000) // Her 1 dakikada bir
    public void generateRandomTransaction() {
        transactionService.generateRandomTransaction()
            .subscribe(
                transaction -> log.info("Scheduled transaction generated: {}", transaction.getTransactionId()),
                error -> log.error("Error generating scheduled transaction: {}", error.getMessage())
            );
    }

    // Her 5 dakikada bir fraudulent transaction üret
    @Scheduled(fixedRate = 300000)
    public void generateFraudulentTransaction() {
        transactionService.generateFraudulentTransaction()
            .subscribe(
                transaction -> log.info("Scheduled fraudulent transaction generated: {}", transaction.getTransactionId()),
                error -> log.error("Error generating scheduled fraudulent transaction: {}", error.getMessage())
            );
    }
} 