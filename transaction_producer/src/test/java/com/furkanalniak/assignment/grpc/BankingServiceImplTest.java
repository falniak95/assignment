package com.furkanalniak.assignment.grpc;

import com.furkanalniak.assignment.model.Customer;
import com.furkanalniak.assignment.service.BranchService;
import com.furkanalniak.assignment.service.CustomerService;
import com.furkanalniak.assignment.service.TransactionService;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankingServiceImplTest {

    @Mock
    private CustomerService customerService;
    
    @Mock
    private TransactionService transactionService;
    
    @Mock
    private BranchService branchService;
    
    @Mock
    private StreamObserver<CustomerResponse> customerResponseObserver;

    private BankingServiceImpl bankingService;

    @BeforeEach
    void setUp() {
        bankingService = new BankingServiceImpl(customerService, transactionService, branchService);
    }

    @Test
    void getCustomerInfo_Success() {
        String customerNo = "10000009";
        Customer mockCustomer = new Customer();
        mockCustomer.setId("679fa76d3e574ff10f5bd0d2");
        mockCustomer.setFirstName("Victor");
        mockCustomer.setLastName("Christiansen");
        mockCustomer.setEmail("sofia.andersen@email.dk");

        CustomerRequest request = CustomerRequest.newBuilder()
            .setCustomerNo(customerNo)
            .build();

        when(customerService.findCustomerByNumber(customerNo))
            .thenReturn(Mono.just(mockCustomer));
        bankingService.getCustomerInfo(request, customerResponseObserver);
        verify(customerResponseObserver, timeout(1000)).onNext(any(CustomerResponse.class));
        verify(customerResponseObserver, timeout(1000)).onCompleted();
        verify(customerResponseObserver, never()).onError(any());
    }

    @Test
    void getCustomerInfo_CustomerNotFound() {
        String customerNo = "99999";
        CustomerRequest request = CustomerRequest.newBuilder()
            .setCustomerNo(customerNo)
            .build();

        when(customerService.findCustomerByNumber(customerNo))
            .thenReturn(Mono.empty());

        bankingService.getCustomerInfo(request, customerResponseObserver);

        verify(customerResponseObserver, timeout(1000)).onError(any());
        verify(customerResponseObserver, never()).onNext(any());
        verify(customerResponseObserver, never()).onCompleted();
    }
} 