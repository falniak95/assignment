package com.furkanalniak.assignment.repository;

import com.furkanalniak.assignment.model.Branch;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BranchRepository extends GenericRepository<Branch> {
    @Aggregation(pipeline = "{ $sample: { size: 1 } }")
    Mono<Branch> findRandomBranch();

    Mono<Branch> findByBranchCode(String branchCode);
} 