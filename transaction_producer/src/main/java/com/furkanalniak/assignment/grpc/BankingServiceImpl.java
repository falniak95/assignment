package com.furkanalniak.assignment.grpc;

import com.furkanalniak.assignment.service.BranchService;
import com.furkanalniak.assignment.service.CustomerService;
import com.furkanalniak.assignment.service.TransactionService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

@GrpcService
public class BankingServiceImpl extends BankingServiceGrpc.BankingServiceImplBase {

  private static final Logger logger = LoggerFactory.getLogger(BankingServiceImpl.class);

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
    logger.info(
        "getCustomerInfo service called. Request: {customerNo:" + request.getCustomerNo() + "}");
    customerService
        .findCustomerByNumber(request.getCustomerNo())
        .doOnNext(
            customer -> {
              logger.info("Customer found: {}", customer);
              CustomerResponse response =
                  CustomerResponse.newBuilder()
                      .setId(customer.getId())
                      .setName(customer.getFirstName().concat(" ").concat(customer.getLastName()))
                      .setEmail(customer.getEmail())
                      .build();
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            })
        .doOnError(
            error -> {
              logger.error("Error in getCustomerInfo: ", error);
              responseObserver.onError(
                  Status.INTERNAL
                      .withDescription("Error processing customer request")
                      .withCause(error)
                      .asException());
            })
        .switchIfEmpty(
            Mono.defer(
                () -> {
                  logger.warn("Customer not found with number: {}", request.getCustomerNo());
                  responseObserver.onError(
                      Status.NOT_FOUND
                          .withDescription(
                              "Customer not found with number: " + request.getCustomerNo())
                          .asException());
                  return Mono.empty();
                }))
        .doFinally(
            signalType ->
                logger.info("getCustomerInfo request completed with signal: {}", signalType))
        .subscribe(
            customer -> logger.debug("Successfully processed customer: {}", customer),
            error -> logger.error("Error processing customer request: ", error));
  }

  @Override
  public void getBranchInfo(
      BranchRequest request, StreamObserver<BranchResponse> responseObserver) {
    logger.info(
        "getBranchInfo service called. Request: {branchCode:" + request.getBranchCode() + "}");
    branchService
        .findBranchByCode(request.getBranchCode())
        .doOnNext(branch -> logger.info("Branch found: {}", branch))
        .map(
            branch ->
                BranchResponse.newBuilder()
                    .setBranchCode(branch.getBranchCode())
                    .setBranchName(branch.getName())
                    .setAddress(branch.getAddress())
                    .build())
        .doOnSuccess(
            response -> {
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            })
        .doOnError(
            error -> {
              logger.error("Error in getBranchInfo: ", error);
              responseObserver.onError(
                  Status.INTERNAL
                      .withDescription("Error processing branch request")
                      .withCause(error)
                      .asException());
            })
        .onErrorResume(
            error -> {
              if (error instanceof RuntimeException) {
                responseObserver.onError(
                    Status.NOT_FOUND
                        .withDescription("Branch not found with code: " + request.getBranchCode())
                        .asException());
              }
              return Mono.empty();
            })
        .subscribe();
  }

  @Override
  public void getTransactionInfo(
      TransactionRequest request, StreamObserver<TransactionResponse> responseObserver) {
    logger.info(
        "getTransactionInfo service called. Request: {transactionID:"
            + request.getTransactionId()
            + "}");
    transactionService
        .findByTransactionId(request.getTransactionId())
        .doOnNext(transaction -> logger.info("Transaction found: {}", transaction))
        .map(
            transaction ->
                TransactionResponse.newBuilder()
                    .setTransactionId(transaction.getId())
                    .setAmount(transaction.getAmount().toString())
                    .setCurrency(transaction.getCurrency())
                    .setSenderId(transaction.getSenderCustomerNumber())
                    .setReceiverId(transaction.getReceiverCustomerNumber())
                    .build())
        .doOnSuccess(
            response -> {
              responseObserver.onNext(response);
              responseObserver.onCompleted();
            })
        .doOnError(
            error -> {
              logger.error("Error in getTransactionInfo: ", error);
              responseObserver.onError(
                  Status.INTERNAL
                      .withDescription("Error processing transaction request")
                      .withCause(error)
                      .asException());
            })
        .onErrorResume(
            error -> {
              if (error instanceof RuntimeException) {
                responseObserver.onError(
                    Status.NOT_FOUND
                        .withDescription(
                            "Transaction not found with id: " + request.getTransactionId())
                        .asException());
              }
              return Mono.empty();
            })
        .subscribe();
  }
}
