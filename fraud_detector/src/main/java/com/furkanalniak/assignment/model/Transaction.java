package com.furkanalniak.assignment.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;
    
    @NotNull
    @Indexed
    private String transactionId;
    
    @NotNull
    private LocalDateTime timestamp;
    
    @NotNull
    private String randomString;
    
    private boolean isFraudulent;
    
    private String fraudReason;
} 