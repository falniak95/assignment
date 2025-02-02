package com.furkanalniak.assignment.model;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "customers")
public class Customer extends AbstractEntity {

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
}
