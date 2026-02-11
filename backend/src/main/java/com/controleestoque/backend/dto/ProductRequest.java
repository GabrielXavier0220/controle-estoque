package com.controleestoque.backend.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductRequest(

        @NotBlank(message = "name é obrigatório")
        @Size(max = 150, message = "name deve ter no máximo 150 caracteres")
        String name,

        @NotBlank(message = "sku é obrigatório")
        @Size(max = 60, message = "sku deve ter no máximo 60 caracteres")
        String sku,

        @Size(max = 2000, message = "description muito grande")
        String description,

        @NotNull(message = "quantity é obrigatório")
        @Min(value = 0, message = "quantity não pode ser negativo")
        Integer quantity,

        @NotNull(message = "minQuantity é obrigatório")
        @Min(value = 0, message = "minQuantity não pode ser negativo")
        Integer minQuantity,

        @NotNull(message = "costPrice é obrigatório")
        @DecimalMin(value = "0.00", inclusive = true, message = "costPrice não pode ser negativo")
        @Digits(integer = 10, fraction = 2, message = "costPrice deve ter no máximo 2 casas decimais")
        BigDecimal costPrice,

        @NotNull(message = "salePrice é obrigatório")
        @DecimalMin(value = "0.00", inclusive = true, message = "salePrice não pode ser negativo")
        @Digits(integer = 10, fraction = 2, message = "salePrice deve ter no máximo 2 casas decimais")
        BigDecimal salePrice
) {}
