package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.Transaction;
import com.furkanalniak.assignment.repository.CustomerRepository;
import com.furkanalniak.assignment.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final CustomerRepository customerRepository;
    private final BranchRepository branchRepository;
    private final TransactionKafkaProducer kafkaProducer;
    private final Random random = new Random();
    private final AtomicInteger sequence = new AtomicInteger(1);

    public Mono<Transaction> generateRandomTransaction() {
        return customerRepository.findAll()
            .collectList()
            .flatMap(customers -> {
                if (customers.size() < 2) {
                    return Mono.error(new RuntimeException("Not enough customers"));
                }

                var sender = customers.get(random.nextInt(customers.size()));
                var receiver = customers.get(random.nextInt(customers.size()));
                
                // Ensure sender and receiver are different
                while (sender.equals(receiver)) {
                    receiver = customers.get(random.nextInt(customers.size()));
                }

                Transaction transaction = new Transaction();
                transaction.setTransactionId("TR" + System.currentTimeMillis());
                transaction.setSenderCustomerNumber(sender.getCustomerNumber());
                transaction.setReceiverCustomerNumber(receiver.getCustomerNumber());
                transaction.setSenderBranchCode(sender.getBranchCode());
                transaction.setReceiverBranchCode(receiver.getBranchCode());
                transaction.setAmount(new BigDecimal(random.nextInt(100000)));
                transaction.setCurrency("DKK");
                transaction.setTimestamp(LocalDateTime.now());
                
                // Generate transaction identifier
                String identifier = generateTransactionIdentifier(sender.getCustomerNumber(), sender.getBranchCode());
                transaction.setTransactionIdentifier(identifier);

                return Mono.just(transaction);
            })
            .doOnNext(kafkaProducer::sendTransaction)
            .doOnNext(t -> log.info("Generated transaction: {}", t.getTransactionId()));
    }

    private String generateTransactionIdentifier(String customerNumber, String branchCode) {
        LocalDateTime now = LocalDateTime.now();
        String dateStr = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String seq = String.format("%04d", sequence.getAndIncrement());
        return String.format("%s_%s_%s_%s", dateStr, branchCode, customerNumber, seq);
    }

    // Generate fraudulent transaction for testing
    public Mono<Transaction> generateFraudulentTransaction() {
        return generateRandomTransaction()
            .map(transaction -> {
                // Make it fraudulent by modifying some fields
                switch (random.nextInt(3)) {
                    case 0:
                        // Future dated transaction
                        transaction.setTimestamp(LocalDateTime.now().plusDays(1));
                        break;
                    case 1:
                        // Invalid branch code
                        transaction.setSenderBranchCode("INVALID");
                        break;
                    case 2:
                        // Mismatched customer-branch
                        transaction.setSenderBranchCode("CPH999");
                        break;
                }
                return transaction;
            });
    }
} 