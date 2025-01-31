package com.furkanalniak.assignment.controller;

import com.furkanalniak.assignment.model.Transaction;
import com.furkanalniak.assignment.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/generate")
    public Mono<Transaction> generateTransaction() {
        return transactionService.generateRandomTransaction();
    }

    @PostMapping("/generate-fraudulent")
    public Mono<Transaction> generateFraudulentTransaction() {
        return transactionService.generateFraudulentTransaction();
    }
} 