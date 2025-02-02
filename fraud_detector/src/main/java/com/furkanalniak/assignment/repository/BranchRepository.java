package com.furkanalniak.assignment.repository;

import com.furkanalniak.assignment.model.Branch;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
@Repository
public interface BranchRepository extends GenericRepository<Branch> {
    Mono<Branch> findByBranchCode(String branchCode);
} 