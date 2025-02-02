package com.furkanalniak.assignment.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transactions")
public class Transaction extends AbstractEntity {

  @NotNull
  @Indexed
  @Schema(description = "Transaction ID", example = "TR123456789")
  private String transactionId;

  @NotNull
  @Schema(description = "Sender Customer Number", example = "10000001")
  private String senderCustomerNumber;

  @NotNull
  @Schema(description = "Receiver Customer Number", example = "10000002")
  private String receiverCustomerNumber;

  @NotNull
  @Schema(description = "Sender Branch Code", example = "34001")
  private String senderBranchCode;

  @NotNull
  @Schema(description = "Receiver Branch Code", example = "34002")
  private String receiverBranchCode;

  @NotNull
  @Schema(description = "Transaction Amount", example = "1000.00")
  private BigDecimal amount;

  @NotNull
  @Schema(description = "Currency Code", example = "TRY")
  private String currency;

  @NotNull
  @Schema(description = "Transaction Timestamp")
  private LocalDateTime timestamp;

  @Schema(description = "Is Transaction Fraudulent", example = "false")
  private boolean fraudulent;

  @Schema(description = "Fraud Reason if Transaction is Fraudulent")
  private String fraudReason;

  @Schema(
      description =
          "Transaction Identifier (Format: YYYYMMDD_SENDER-BRANCH_SENDER-CUSTOMER_SEQUENCE)",
      example = "20240131_34001_10000001_001")
  private String transactionIdentifier;
}
