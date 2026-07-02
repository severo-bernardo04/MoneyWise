package com.bernardo.moneywise.dto;

import com.bernardo.moneywise.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequestDTO {

    @NotBlank(message = "Descrição é Obrigatória")
    @Size(min = 3, max = 150, message = "Descrição deve ter entre 3 a 150 caracteres")
    private String description;

    @NotNull(message = "Quantidade é necessaria")
    @DecimalMin(value = "0.01", message = " Quantidade deve ser positiva")
    private BigDecimal amount;

    @NotNull(message = "Tipo é Obrigatório")
    private TransactionType transactionType;

    @NotNull(message = "Data é Obrigatória")
    private LocalDate transactionDate;

    @NotNull(message = "Id da Categoria é Obrigatório")
    private Long categoryId;


}
