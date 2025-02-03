package com.furkanalniak.assignment.repository;

import com.furkanalniak.assignment.model.Customer;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends GenericRepository<Customer> {
    @Aggregation(pipeline = "{ $sample: { size: 1 } }")
    Mono<Customer> findRandomCustomer();

    Mono<Customer> findCustomerByCustomerNumber(String customerNumber);
}
