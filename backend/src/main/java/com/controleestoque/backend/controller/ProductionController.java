package com.controleestoque.backend.controller;

import com.controleestoque.backend.dto.ProductionSuggestionResponse;
import com.controleestoque.backend.service.ProductionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/production")
public class ProductionController {

    private final ProductionService productionService;

    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    /**
     * GET /production/suggestions
     * Retorna a lista de produtos que podem ser produzidos com o estoque atual
     * Ordenado por maior valor total */

    @GetMapping("/suggestions")
    public ProductionSuggestionResponse getProductionSuggestions() {
        return productionService.calculateProductionSuggestion();
    }
}
