package com.furkanalniak.assignment.repository;

import com.furkanalniak.assignment.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Mono<Transaction> findByTransactionId(String transactionId);
    Flux<Transaction> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
} 