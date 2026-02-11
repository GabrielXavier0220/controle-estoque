package com.controleestoque.backend.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Contém a lista de produtos que podem ser produzidos e o valor total*/

public record ProductionSuggestionResponse(
        List<ProductionSuggestionItem> suggestedProducts,
        BigDecimal totalValue,
        String message
) {
}
