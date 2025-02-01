package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.Transaction;
import com.furkanalniak.assignment.repository.BranchRepository;
import com.furkanalniak.assignment.repository.CustomerRepository;
import com.furkanalniak.assignment.repository.TransactionRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
public class FraudDetectionService {
    private final BranchRepository branchRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;
    protected final Logger logger = LogManager.getLogger(FraudDetectionService.class);

    @Autowired
    public FraudDetectionService(BranchRepository branchRepository, CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.branchRepository = branchRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    private static final BigDecimal HIGH_AMOUNT_THRESHOLD = new BigDecimal("50000.00");
    private static final LocalTime BUSINESS_HOURS_START = LocalTime.of(9, 0);
    private static final LocalTime BUSINESS_HOURS_END = LocalTime.of(17, 0);

    public Mono<Transaction> detectFraud(Transaction transaction) {
        return Mono.just(transaction)
            .flatMap(this::validateTransactionIdentifier)
            .flatMap(this::validateTimestamp)
            .flatMap(this::validateBranches)
            .flatMap(this::validateCustomers)
            .flatMap(this::validateAmount)
            .flatMap(this::validateLocationPattern);
    }

    private Mono<Transaction> validateTransactionIdentifier(Transaction transaction) {
        try {
            String[] parts = transaction.getTransactionIdentifier().split("_");
            if (parts.length != 4) {
                return markAsFraudulent(transaction, "Invalid transaction identifier format");
            }

            // Validate date format
            String dateStr = parts[0];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
            LocalDateTime identifierDate = LocalDateTime.parse(dateStr + " 000000", formatter);

            if (!identifierDate.toLocalDate().equals(transaction.getTimestamp().toLocalDate())) {
                return markAsFraudulent(transaction, "Transaction date mismatch");
            }

            // Validate sender branch and customer
            if (!parts[1].equals(transaction.getSenderBranchCode())) {
                return markAsFraudulent(transaction, "Sender branch mismatch");
            }
            if (!parts[2].equals(transaction.getSenderCustomerNumber())) {
                return markAsFraudulent(transaction, "Sender customer mismatch");
            }

            return Mono.just(transaction);
        } catch (Exception e) {
            return markAsFraudulent(transaction, "Invalid transaction identifier: " + e.getMessage());
        }
    }

    private Mono<Transaction> validateTimestamp(Transaction transaction) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime transactionTime = transaction.getTimestamp();

        if (transactionTime.isAfter(now)) {
            return markAsFraudulent(transaction, "Future dated transaction");
        }

        if (transactionTime.isBefore(now.minusHours(24))) {
            return markAsFraudulent(transaction, "Transaction too old");
        }

        LocalTime time = transactionTime.toLocalTime();
        if (transaction.getAmount().compareTo(HIGH_AMOUNT_THRESHOLD) > 0 &&
            (time.isBefore(BUSINESS_HOURS_START) || time.isAfter(BUSINESS_HOURS_END))) {
            return markAsFraudulent(transaction, "High value transaction outside business hours");
        }

        return Mono.just(transaction);
    }

    private Mono<Transaction> validateBranches(Transaction transaction) {
        return branchRepository.findByBranchCode(transaction.getSenderBranchCode())
            .zipWith(branchRepository.findByBranchCode(transaction.getReceiverBranchCode()))
            .flatMap(tuple -> {
                var senderBranch = tuple.getT1();
                var receiverBranch = tuple.getT2();

                if (!senderBranch.isActive() || !receiverBranch.isActive()) {
                    return markAsFraudulent(transaction, "Inactive branch involved");
                }

                return Mono.just(transaction);
            })
            .switchIfEmpty(markAsFraudulent(transaction, "Invalid branch codes"));
    }

    private Mono<Transaction> validateCustomers(Transaction transaction) {
        return customerRepository.findByCustomerNumber(transaction.getSenderCustomerNumber())
            .zipWith(customerRepository.findByCustomerNumber(transaction.getReceiverCustomerNumber()))
            .flatMap(tuple -> {
                var sender = tuple.getT1();
                var receiver = tuple.getT2();

                if (!sender.isActive() || !receiver.isActive()) {
                    return markAsFraudulent(transaction, "Inactive customer involved");
                }

                if (!sender.getBranchCode().equals(transaction.getSenderBranchCode())) {
                    return markAsFraudulent(transaction, "Sender branch mismatch with registered branch");
                }

                return Mono.just(transaction);
            })
            .switchIfEmpty(markAsFraudulent(transaction, "Invalid customer numbers"));
    }

    private Mono<Transaction> validateAmount(Transaction transaction) {
        return transactionRepository.findBySenderCustomerNumberAndTimestampAfter(
                transaction.getSenderCustomerNumber(),
                LocalDateTime.now().minusHours(24)
            )
            .collectList()
            .flatMap(recentTransactions -> {
                BigDecimal totalAmount = recentTransactions.stream()
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (totalAmount.add(transaction.getAmount()).compareTo(HIGH_AMOUNT_THRESHOLD) > 0) {
                    return markAsFraudulent(transaction, "Daily transaction limit exceeded");
                }

                return Mono.just(transaction);
            });
    }

    private Mono<Transaction> validateLocationPattern(Transaction transaction) {
        return transactionRepository.findBySenderCustomerNumberAndTimestampAfter(
                transaction.getSenderCustomerNumber(),
                LocalDateTime.now().minusMinutes(30)
            )
            .collectList()
            .flatMap(recentTransactions -> {
                long distinctBranchCount = recentTransactions.stream()
                    .map(Transaction::getSenderBranchCode)
                    .distinct()
                    .count();

                if (distinctBranchCount >= 3) {
                    return markAsFraudulent(transaction, "Suspicious multi-branch activity");
                }

                return Mono.just(transaction);
            });
    }

    private Mono<Transaction> markAsFraudulent(Transaction transaction, String reason) {
        transaction.setFraudulent(true);
        transaction.setFraudReason(reason);
        logger.warn("Fraud detected: {} - {}", transaction.getTransactionId(), reason);
        return Mono.just(transaction);
    }
} 