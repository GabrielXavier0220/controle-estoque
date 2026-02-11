package com.controleestoque.backend.dto;

import java.math.BigDecimal;

/**
 * DTO que representa um produto que pode ser produzido
 * com as matérias-primas disponíveis em estoque */

public record ProductionSuggestionItem(
        Long productId,
        String productName,
        String productSku,
        Integer maxQuantity,        // Quantidade máxima que pode ser produzida
        BigDecimal unitPrice,       // Preço de venda unitário
        BigDecimal totalValue       // Valor total (maxQuantity * unitPrice)
) {
}
