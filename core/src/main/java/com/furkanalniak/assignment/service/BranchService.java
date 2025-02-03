package com.furkanalniak.assignment.service;

import com.furkanalniak.assignment.model.Branch;
import com.furkanalniak.assignment.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BranchService extends AbstractService<Branch> {
  private final BranchRepository branchRepository;

  @Autowired
  public BranchService(BranchRepository branchRepository) {
    this.branchRepository = branchRepository;
  }

  public Mono<Branch> findBranchByCode(String code) {
    return branchRepository.findByBranchCode(code.trim());
  }

  public Mono<Branch> findRandomBranch() {
    return branchRepository.findRandomBranch();
  }

}
