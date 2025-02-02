package com.furkanalniak.assignment.controller;

import com.furkanalniak.assignment.manager.TransactionManager;
import com.furkanalniak.assignment.model.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Transaction Operations API")
public class TransactionController {
  private final TransactionManager transactionManager;

  @Autowired
  public TransactionController(TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  @Operation(summary = "Generate random transaction")
  @PostMapping("/generate")
  public Mono<Transaction> generateTransaction() {
    return transactionManager.generateRandomTransaction();
  }

  @Operation(summary = "Generate fraudulent transaction")
  @PostMapping("/generate-fraudulent")
  public Mono<Transaction> generateFraudulentTransaction() {
    return transactionManager.generateFraudulentTransaction();
  }
}
