package com.furkanalniak.assignment.controller;

import com.furkanalniak.assignment.model.Transaction;
import com.furkanalniak.assignment.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Transaction Operations API")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService=transactionService;
    }

    @Operation(summary = "Generate random transaction")
    @PostMapping("/generate")
    public Transaction generateTransaction() {
        return transactionService.generateRandomTransaction();
    }

    @Operation(summary = "Generate fraudulent transaction")
    @PostMapping("/generate-fraudulent")
    public Transaction generateFraudulentTransaction() {
        return transactionService.generateFraudulentTransaction();
    }
} 