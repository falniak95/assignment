package com.furkanalniak.assignment.repository;

import com.furkanalniak.assignment.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
  Mono<Customer> findByCustomerNumber(String customerNumber);
}
