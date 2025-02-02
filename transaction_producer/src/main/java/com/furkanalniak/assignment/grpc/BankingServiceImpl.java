package com.furkanalniak.assignment.grpc;

import com.furkanalniak.assignment.model.Branch;
import com.furkanalniak.assignment.model.Customer;
import com.furkanalniak.assignment.model.Transaction;
import com.furkanalniak.assignment.service.BranchService;
import com.furkanalniak.assignment.service.CustomerService;
import com.furkanalniak.assignment.service.TransactionService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankingServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase {

  private final CustomerService customerService;

  private final TransactionService transactionService;
  private final BranchService branchService;

  @Autowired
  public BankingServiceImpl(
      CustomerService customerService,
      TransactionService transactionService,
      BranchService branchService) {
    this.customerService = customerService;
    this.transactionService = transactionService;
    this.branchService = branchService;
  }

  @Override
  public void getCustomerInfo(
      CustomerRequest request, StreamObserver<CustomerResponse> responseObserver) {
    Customer customer = customerService.findCustomerByNumber(request.getCustomerNo()).block();

    CustomerResponse response =
        CustomerResponse.newBuilder()
            .setId(customer.getId())
            .setName(customer.getFirstName().concat(" ").concat(customer.getLastName()))
            .setEmail(customer.getEmail())
            .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getBranchInfo(
      BranchRequest request, StreamObserver<BranchResponse> responseObserver) {
    Branch branch = branchService.findBranchByCode(request.getBranchCode()).block();
    BranchResponse response =
        BranchResponse.newBuilder()
            .setBranchCode(branch.getBranchCode())
            .setBranchName(branch.getName())
            .setAddress(branch.getAddress())
            .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getTransactionInfo(
      TransactionRequest request, StreamObserver<TransactionResponse> responseObserver) {
    Transaction transaction =
        transactionService.findByTransactionId(request.getTransactionId()).block();

    TransactionResponse response =
        TransactionResponse.newBuilder()
            .setTransactionId(transaction.getId())
            .setAmount(transaction.getAmount().toString())
            .setCurrency(transaction.getCurrency())
            .setSenderId(transaction.getSenderCustomerNumber())
            .setReceiverId(transaction.getReceiverCustomerNumber())
            .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
