package com.furkanalniak.assignment.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Document(collection = "branches")
public class Branch {
    @Id
    private String id;
    
    @Indexed(unique = true)
    private String branchCode;
    
    private String name;
    private String city;
    private String district;
    private String address;
    private String phone;
    private boolean isActive;
} 