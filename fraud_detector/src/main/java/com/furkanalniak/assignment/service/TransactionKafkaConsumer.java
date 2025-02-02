package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.Transaction;
import com.furkanalniak.assignment.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class TransactionKafkaConsumer {
    private static final Logger logger = LogManager.getLogger(TransactionKafkaConsumer.class);
    private final TransactionRepository transactionRepository;
    private final FraudDetectionService fraudDetectionService;

    @Autowired
    public TransactionKafkaConsumer(TransactionRepository transactionRepository,
                                  FraudDetectionService fraudDetectionService) {
        this.transactionRepository = transactionRepository;
        this.fraudDetectionService = fraudDetectionService;
        logger.info("TransactionKafkaConsumer initialized");
    }

    @PostConstruct
    public void init() {
        logger.info("TransactionKafkaConsumer initialized with groupId: {} and topic: {}",
            "${spring.kafka.consumer.group-id}", "${spring.kafka.topics.transaction-events}");
    }

    @KafkaListener(
        topics = "${spring.kafka.topics.transaction-events}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(Transaction transaction, Acknowledgment ack) {
        logger.info("Received transaction: {}", transaction);
        try {
            if (transaction == null) {
                logger.error("Received null transaction");
                ack.acknowledge();
                return;
            }

            fraudDetectionService.detectFraud(transaction)
                .doOnNext(processedTransaction -> 
                    logger.info("Fraud detection completed for: {}", processedTransaction.getTransactionId()))
                .flatMap(transactionRepository::save)
                .doOnSuccess(savedTransaction -> {
                    logger.info("Transaction processed and saved: {}", savedTransaction.getTransactionId());
                    ack.acknowledge();
                })
                .doOnError(error -> {
                    logger.error("Error processing transaction: {}", error.getMessage(), error);
                    ack.acknowledge();
                })
                .subscribe();
        } catch (Exception e) {
            logger.error("Error in Kafka listener: {}", e.getMessage(), e);
            ack.acknowledge();
        }
    }
} 