package com.furkanalniak.assignment.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDate;

@Data
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String customerNumber;
    
    private String firstName;
    private String lastName;
    private String identityNumber;
    private LocalDate birthDate;
    private String phone;
    private String email;
    private String address;
    private String branchCode;
    private LocalDate registrationDate;
    private boolean isActive;
} 