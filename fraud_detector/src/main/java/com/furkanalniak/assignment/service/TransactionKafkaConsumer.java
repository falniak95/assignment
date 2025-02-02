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
        logger.info("TransactionKafkaConsumer initialized");
    }

    @KafkaListener(
        topics = "${spring.kafka.topics.transaction-events}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(Transaction transaction, Acknowledgment ack) {
        try {
            logger.info("Consumer started - Topic: {}, Group: {}",
                "${spring.kafka.topics.transaction-events}", 
                "${spring.kafka.consumer.group-id}");
            logger.info("Received transaction with ID: {}", 
                transaction != null ? transaction.getTransactionId() : "null");
            
            if (transaction == null) {
                logger.error("Received null transaction");
                return;
            }

            fraudDetectionService.detectFraud(transaction)
                .doOnSuccess(processedTransaction -> {
                    transactionRepository.save(processedTransaction);
                    logger.info("Successfully processed and saved transaction: {}", processedTransaction);
                    ack.acknowledge();
                    logger.info("Transaction acknowledged: {}", processedTransaction.getTransactionId());
                })
                .doOnError(error -> {
                    logger.error("Error processing transaction: {}", error.getMessage(), error);
                })
                .subscribe();
        } catch (Exception e) {
            logger.error("Error in Kafka listener: {}", e.getMessage(), e);
        }
    }
} 