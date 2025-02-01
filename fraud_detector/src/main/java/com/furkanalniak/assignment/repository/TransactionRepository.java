package com.furkanalniak.assignment.repository;

import com.furkanalniak.assignment.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    
    @Query("{'senderCustomerNumber': ?0, 'timestamp': {$gt: ?1}}")
    Flux<Transaction> findBySenderCustomerNumberAndTimestampAfter(String senderCustomerNumber, LocalDateTime timestamp);
} 