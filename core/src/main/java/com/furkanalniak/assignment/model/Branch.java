package com.furkanalniak.assignment.model;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "branches")
public class Branch extends AbstractEntity {

  @Indexed(unique = true)
  private String branchCode;

  private String name;
  private String city;
  private String district;
  private String address;
  private String phone;
}
