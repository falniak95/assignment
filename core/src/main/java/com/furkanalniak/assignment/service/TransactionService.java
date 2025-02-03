package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.Transaction;
import com.furkanalniak.assignment.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService extends AbstractService<Transaction> {
  private static final Logger logger = LogManager.getLogger(TransactionService.class);

  private final TransactionRepository transactionRepository;
  private final CustomerService customerService;
  private final BranchService branchService;
  private final Random random = new Random();
  private final AtomicInteger sequence = new AtomicInteger(1);

  @Autowired
  public TransactionService(TransactionRepository transactionRepository, CustomerService customerService, BranchService branchService) {
    this.transactionRepository = transactionRepository;
    this.customerService = customerService;
    this.branchService = branchService;
  }

  public Mono<Transaction> findByTransactionId(String transactionId) {
    return transactionRepository.findByTransactionId(transactionId);
  }

  public Flux<Transaction> findBySenderCustomerNumberAndTimestampAfter(
      String customerNo, LocalDateTime timestamp) {
    return transactionRepository.findBySenderCustomerNumberAndTimestampAfter(customerNo, timestamp);
  }

  public Mono<Transaction> generateRandomTransaction() {
    return transactionRepository.save(createRandomTransaction())
        .doOnNext(
            transaction -> {
              logger.info("Generated random transaction: {}", transaction.getTransactionId());
            });
  }

  public Mono<Transaction> generateFraudulentTransaction() {
    return transactionRepository
        .save(createFraudulentTransaction()) // Mono<Transaction> döner
        .doOnNext(
            savedTransaction -> {
              logger.info(
                  "Generated fraudulent transaction: {}", savedTransaction.getTransactionId());
            });
  }

  private Transaction createRandomTransaction() {
    String senderCustomerNumber = customerService.findRandomCustomer().block().getCustomerNumber();
    String receiverCustomerNumber = customerService.findRandomCustomer().block().getCustomerNumber();
    do {
      receiverCustomerNumber = customerService.findRandomCustomer().block().getCustomerNumber();
    } while (senderCustomerNumber.equals(receiverCustomerNumber));

    String senderBranchCode = branchService.findRandomBranch().block().getBranchCode();
    String receiverBranchCode = branchService.findRandomBranch().block().getBranchCode();

    Transaction transaction =
        Transaction.builder()
            .transactionId("DK" + System.currentTimeMillis())
            .senderCustomerNumber(senderCustomerNumber)
            .receiverCustomerNumber(receiverCustomerNumber)
            .senderBranchCode(senderBranchCode)
            .receiverBranchCode(receiverBranchCode)
            .amount(generateRandomAmount())
            .currency("DKK")
            .timestamp(LocalDateTime.now())
            .transactionIdentifier(
                generateTransactionIdentifier(senderCustomerNumber, senderBranchCode))
            .fraudulent(false)
            .build();

    return transaction;
  }

  private Transaction createFraudulentTransaction() {
    Transaction transaction = createRandomTransaction();
    transaction =
        Transaction.builder()
            .transactionId(transaction.getTransactionId())
            .senderCustomerNumber(transaction.getSenderCustomerNumber())
            .receiverCustomerNumber(transaction.getReceiverCustomerNumber())
            .senderBranchCode(transaction.getSenderBranchCode())
            .receiverBranchCode(transaction.getReceiverBranchCode())
            .amount(transaction.getAmount())
            .currency(transaction.getCurrency())
            .timestamp(transaction.getTimestamp())
            .transactionIdentifier(transaction.getTransactionIdentifier())
            .fraudulent(true)
            .build();

    switch (random.nextInt(3)) {
      case 0:
        // Invalid timestamp
        transaction =
            Transaction.builder()
                .transactionId(transaction.getTransactionId())
                .senderCustomerNumber(transaction.getSenderCustomerNumber())
                .receiverCustomerNumber(transaction.getReceiverCustomerNumber())
                .senderBranchCode(transaction.getSenderBranchCode())
                .receiverBranchCode(transaction.getReceiverBranchCode())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .timestamp(LocalDateTime.now().plusDays(1))
                .transactionIdentifier(transaction.getTransactionIdentifier())
                .fraudulent(true)
                .fraudReason("Future dated transaction")
                .build();
        break;
      case 1:
        // Invalid branch code
        transaction =
            Transaction.builder()
                .transactionId(transaction.getTransactionId())
                .senderCustomerNumber(transaction.getSenderCustomerNumber())
                .receiverCustomerNumber(transaction.getReceiverCustomerNumber())
                .senderBranchCode("99999")
                .receiverBranchCode(transaction.getReceiverBranchCode())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .timestamp(transaction.getTimestamp())
                .transactionIdentifier(transaction.getTransactionIdentifier())
                .fraudulent(true)
                .fraudReason("Invalid branch code")
                .build();
        break;
      case 2:
        // High amount transaction
        transaction =
            Transaction.builder()
                .transactionId(transaction.getTransactionId())
                .senderCustomerNumber(transaction.getSenderCustomerNumber())
                .receiverCustomerNumber(transaction.getReceiverCustomerNumber())
                .senderBranchCode(transaction.getSenderBranchCode())
                .receiverBranchCode(transaction.getReceiverBranchCode())
                .amount(new BigDecimal("1000000.00"))
                .currency(transaction.getCurrency())
                .timestamp(transaction.getTimestamp())
                .transactionIdentifier(transaction.getTransactionIdentifier())
                .fraudulent(true)
                .fraudReason("Unusually high amount")
                .build();
        break;
    }

    return transaction;
  }

  private String generateTransactionIdentifier(String customerNumber, String branchCode) {
    LocalDateTime now = LocalDateTime.now();
    String dateStr = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String seq = String.format("%03d", sequence.getAndIncrement());
    return String.format("%s_%s_%s_%s", dateStr, branchCode, customerNumber, seq);
  }

  private BigDecimal generateRandomAmount() {
    return new BigDecimal(random.nextInt(10000))
        .add(new BigDecimal(random.nextInt(100)).divide(new BigDecimal(100)))
        .setScale(2, BigDecimal.ROUND_HALF_UP);
  }

  private <T> T getRandomElement(List<T> list) {
    return list.get(random.nextInt(list.size()));
  }
}
