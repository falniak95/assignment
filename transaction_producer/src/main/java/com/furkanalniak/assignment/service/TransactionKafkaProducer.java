package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.Transaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.kafka.support.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class TransactionKafkaProducer {
    private final KafkaTemplate<String, Transaction> kafkaTemplate;
    private static final String TOPIC = "transaction-events";

    public TransactionKafkaProducer(KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransaction(Transaction transaction) {
        CompletableFuture<SendResult<String, Transaction>> future = 
            kafkaTemplate.send(TOPIC, transaction.getTransactionId(), transaction);
            
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Transaction sent successfully: {}", transaction.getTransactionId());
            } else {
                log.error("Failed to send transaction: {}", ex.getMessage());
            }
        });
    }
} 