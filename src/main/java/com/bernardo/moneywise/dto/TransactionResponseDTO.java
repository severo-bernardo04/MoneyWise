package com.bernardo.moneywise.dto;

import com.bernardo.moneywise.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {

    private Long id;
    private String description;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDate transactionDate;
    private String categoryName;
}
