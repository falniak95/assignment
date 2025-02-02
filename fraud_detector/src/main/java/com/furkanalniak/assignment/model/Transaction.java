package com.furkanalniak.assignment.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    
    @NotNull
    @Indexed
    private String transactionId;
    
    @NotNull
    private String senderCustomerNumber;
    
    @NotNull
    private String receiverCustomerNumber;
    
    @NotNull
    private String senderBranchCode;
    
    @NotNull
    private String receiverBranchCode;
    
    @NotNull
    private BigDecimal amount;
    
    @NotNull
    private String currency;
    
    @NotNull
    private LocalDateTime timestamp;
    
    private boolean isFraudulent;
    private String fraudReason;
    
    private String transactionIdentifier;
} 