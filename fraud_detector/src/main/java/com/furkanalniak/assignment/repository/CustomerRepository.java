package com.furkanalniak.assignment.repository;

import com.furkanalniak.assignment.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
    Mono<Customer> findByCustomerNumber(String customerNumber);
} 