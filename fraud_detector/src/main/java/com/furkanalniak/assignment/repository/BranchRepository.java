package com.furkanalniak.assignment.repository;

import com.furkanalniak.assignment.model.Branch;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BranchRepository extends ReactiveMongoRepository<Branch, String> {
    Mono<Branch> findByBranchCode(String branchCode);
} 