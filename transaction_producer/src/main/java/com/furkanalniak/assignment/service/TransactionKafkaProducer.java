package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.Transaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class TransactionKafkaProducer {
    private static final Logger logger = LogManager.getLogger(TransactionKafkaProducer.class);
    
    @Value("${spring.kafka.topics.transaction-events}")
    private String topic;
    
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    public TransactionKafkaProducer(KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransaction(Transaction transaction) {
        logger.info("Sending transaction to topic {}: {}", topic, transaction);
        try {
            CompletableFuture<SendResult<String, Transaction>> future = 
                kafkaTemplate.send(topic, transaction.getTransactionId(), transaction);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Successfully sent transaction: {}", transaction.getTransactionId());
                } else {
                    logger.error("Failed to send transaction: {}", ex.getMessage(), ex);
                }
            });
        } catch (Exception e) {
            logger.error("Error while sending transaction: {}", e.getMessage(), e);
        }
    }
} 