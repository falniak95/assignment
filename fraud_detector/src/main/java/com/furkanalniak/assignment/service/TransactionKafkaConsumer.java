package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.Transaction;
import com.furkanalniak.assignment.repository.TransactionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@EnableKafka
@Slf4j
@RequiredArgsConstructor
public class TransactionKafkaConsumer {
    private final TransactionRepository transactionRepository;
    private final FraudDetectionService fraudDetectionService;

    @KafkaListener(topics = "transaction-events", groupId = "fraud-detector-group")
    public void consumeTransaction(Transaction transaction) {
        log.info("Received transaction: {}", transaction.getTransactionId());
        
        fraudDetectionService.detectFraud(transaction)
            .flatMap(transactionRepository::save)
            .subscribe(
                savedTransaction -> log.info("Transaction processed: {}, Fraudulent: {}", 
                    savedTransaction.getTransactionId(), 
                    savedTransaction.isFraudulent()),
                error -> log.error("Error processing transaction: {}", error.getMessage())
            );
    }
} 