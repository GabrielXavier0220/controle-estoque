package com.controleestoque.backend.service;

import com.controleestoque.backend.dto.ProductionSuggestionItem;
import com.controleestoque.backend.dto.ProductionSuggestionResponse;
import com.controleestoque.backend.entity.Product;
import com.controleestoque.backend.entity.ProductRawMaterial;
import com.controleestoque.backend.repository.ProductRepository;
import com.controleestoque.backend.repository.ProductRawMaterialRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductionService {

    private final ProductRepository productRepository;
    private final ProductRawMaterialRepository productRawMaterialRepository;

    public ProductionService(ProductRepository productRepository,
                            ProductRawMaterialRepository productRawMaterialRepository) {
        this.productRepository = productRepository;
        this.productRawMaterialRepository = productRawMaterialRepository;
    }

    /**
     * Calcula quais produtos podem ser produzidos com as matérias-primas disponíveis
     * Prioriza produtos de maior valor
     * 
     * Lógica:
     * 1. Para cada produto, verifica quantas unidades podem ser feitas (limitado pela matéria-prima que acabar primeiro)
     * 2. Ordena produtos por valor de venda (maior primeiro)
     * 3. Calcula o valor total que será obtido
     */
    public ProductionSuggestionResponse calculateProductionSuggestion() {
        
        // Buscar todos os produtos
        List<Product> allProducts = productRepository.findAll();
        
        List<ProductionSuggestionItem> suggestions = new ArrayList<>();
        
        // Para cada produto, calcular quantas unidades podem ser produzidas
        for (Product product : allProducts) {
            
            // Buscar matérias-primas necessárias para este produto
            List<ProductRawMaterial> rawMaterials = productRawMaterialRepository
                    .findByProduct_Id(product.getId());
            
            // Se o produto não tem matérias-primas associadas, pula
            if (rawMaterials.isEmpty()) {
                continue;
            }
            
            // Calcular a quantidade máxima que pode ser produzida
            Integer maxQuantity = calculateMaxProductionQuantity(rawMaterials);
            
            // Se não pode produzir nenhuma unidade, pula
            if (maxQuantity == 0) {
                continue;
            }
            
            // Calcular valor total (quantidade * preço unitário)
            BigDecimal totalValue = product.getSalePrice()
                    .multiply(BigDecimal.valueOf(maxQuantity));
            
            // Criar item de sugestão
            ProductionSuggestionItem item = new ProductionSuggestionItem(
                    product.getId(),
                    product.getName(),
                    product.getSku(),
                    maxQuantity,
                    product.getSalePrice(),
                    totalValue
            );
            
            suggestions.add(item);
        }
        
        // ORDENAR por valor total (maior valor primeiro) - PRIORIZAÇÃO
        suggestions.sort(Comparator.comparing(ProductionSuggestionItem::totalValue).reversed());
        
        // Calcular valor total geral
        BigDecimal grandTotal = suggestions.stream()
                .map(ProductionSuggestionItem::totalValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        

        String message = suggestions.isEmpty() 
                ? "Nenhum produto pode ser produzido com o estoque atual."
                : "Sugestão de produção baseada no estoque disponível (ordenado por maior valor).";
        
        return new ProductionSuggestionResponse(suggestions, grandTotal, message);
    }
    
    /**
     * Calcula a quantidade máxima que pode ser produzida de um produto
     * baseado no estoque disponível de cada matéria-prima
     * 
     * Lógica: A quantidade máxima é limitada pela matéria-prima que "acabar primeiro"
     * 
     * Exemplo:
     * - Bolo precisa de: 500g farinha, 200g açúcar, 150g chocolate
     * - Estoque: farinha=1000g, açúcar=500g, chocolate=300g
     * - Farinha permite: 1000/500 = 2 bolos
     * - Açúcar permite: 500/200 = 2 bolos (arredonda pra baixo)
     * - Chocolate permite: 300/150 = 2 bolos
     * - Resultado: pode fazer 2 bolos (limitado pelo que acabar primeiro)
     */
    private Integer calculateMaxProductionQuantity(List<ProductRawMaterial> rawMaterials) {
        
        Integer maxQuantity = Integer.MAX_VALUE;
        
        for (ProductRawMaterial prm : rawMaterials) {
            
            Integer stockAvailable = prm.getRawMaterial().getStockQuantity();
            Integer requiredPerUnit = prm.getRequiredQuantity();
            
            // Calcular quantas unidades do produto podem ser feitas com esta matéria-prima
            Integer possibleQuantity = stockAvailable / requiredPerUnit;
            
            // O produto só pode ser feito na quantidade da matéria-prima mais limitante
            if (possibleQuantity < maxQuantity) {
                maxQuantity = possibleQuantity;
            }
        }
        
        // Se nenhuma matéria-prima foi processada, retorna 0
        return maxQuantity == Integer.MAX_VALUE ? 0 : maxQuantity;
    }
}
