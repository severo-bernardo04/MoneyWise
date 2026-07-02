package com.bernardo.moneywise.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDTO {

    @NotBlank(message = "Nome da Categoria é Obrigatório")
    private String categoryName;

}
