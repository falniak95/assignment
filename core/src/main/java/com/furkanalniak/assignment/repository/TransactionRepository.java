package com.furkanalniak.assignment.repository;

import com.furkanalniak.assignment.model.Transaction;
import java.time.LocalDateTime;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TransactionRepository extends GenericRepository<Transaction> {

  @Query("{'senderCustomerNumber': ?0, 'timestamp': {$gt: ?1}}")
  Flux<Transaction> findBySenderCustomerNumberAndTimestampAfter(
      String senderCustomerNumber, LocalDateTime timestamp);

  Mono<Transaction> findByTransactionId(String transactionId);
}
