package com.furkanalniak.assignment.repository;

import com.furkanalniak.assignment.model.Customer;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends GenericRepository<Customer> {
  Mono<Customer> findByCustomerNumber(String customerNumber);
}
