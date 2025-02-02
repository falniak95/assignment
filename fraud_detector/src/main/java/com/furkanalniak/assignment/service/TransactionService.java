package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.Transaction;
import com.furkanalniak.assignment.repository.TransactionRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class TransactionService extends AbstractService<Transaction> {
  private final TransactionRepository transactionRepository;

  @Autowired
  public TransactionService(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  public Flux<Transaction> findBySenderCustomerNumberAndTimestampAfter(
      String customerNo, LocalDateTime timestamp) {
    return transactionRepository.findBySenderCustomerNumberAndTimestampAfter(customerNo, timestamp);
  }
}
