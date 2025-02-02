package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.Customer;
import com.furkanalniak.assignment.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerService extends AbstractService<Customer> {
  private final CustomerRepository customerRepository;

  @Autowired
  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public Mono<Customer> findCustomerByNumber(String customerNo) {
    return customerRepository.findCustomerByCustomerNumber(customerNo);
  }
}
