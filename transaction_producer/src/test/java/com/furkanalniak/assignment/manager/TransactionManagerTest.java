package com.furkanalniak.assignment.manager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.furkanalniak.assignment.model.Transaction;
import com.furkanalniak.assignment.service.TransactionKafkaProducer;
import com.furkanalniak.assignment.service.TransactionService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class TransactionManagerTest {

  @Mock private TransactionService transactionService;

  @Mock private TransactionKafkaProducer kafkaProducer;

  private TransactionManager transactionManager;

  @BeforeEach
  void setUp() {
    transactionManager = new TransactionManager(transactionService, kafkaProducer);
  }

  @Test
  void generateFraudulentTransaction_Success() {
    Transaction mockTransaction = new Transaction();
    mockTransaction.setAmount(new BigDecimal("29999.00"));
    mockTransaction.setCurrency("DKK");
    mockTransaction.setTimestamp(LocalDateTime.of(2025, 1, 2, 23, 05, 01));
    when(transactionService.generateFraudulentTransaction())
        .thenReturn(Mono.just(mockTransaction));
    doNothing().when(kafkaProducer).sendTransaction(any(Transaction.class));

    StepVerifier.create(transactionManager.generateFraudulentTransaction())
        .expectNext(mockTransaction)
        .verifyComplete();

    verify(kafkaProducer).sendTransaction(mockTransaction);
  }

  @Test
  void generateRandomTransaction_Success() {
    Transaction mockTransaction = new Transaction();
    mockTransaction.setAmount(new BigDecimal("100.00"));
    mockTransaction.setCurrency("USD");
    mockTransaction.setTimestamp(LocalDateTime.now());
    when(transactionService.generateRandomTransaction())
        .thenReturn(Mono.just(mockTransaction));
    doNothing().when(kafkaProducer).sendTransaction(any(Transaction.class));

    StepVerifier.create(transactionManager.generateRandomTransaction())
        .expectNext(mockTransaction)
        .verifyComplete();

    verify(kafkaProducer).sendTransaction(mockTransaction);
  }
}
