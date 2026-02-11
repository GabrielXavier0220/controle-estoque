package com.controleestoque.backend.service;

import com.controleestoque.backend.dto.ProductionSuggestionResponse;
import com.controleestoque.backend.entity.Product;
import com.controleestoque.backend.entity.ProductRawMaterial;
import com.controleestoque.backend.entity.RawMaterial;
import com.controleestoque.backend.repository.ProductRawMaterialRepository;
import com.controleestoque.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductRawMaterialRepository productRawMaterialRepository;

    @InjectMocks
    private ProductionService productionService;

    private Product boloDeChocolate;
    private Product paoFrances;
    private RawMaterial farinha;
    private RawMaterial acucar;
    private RawMaterial chocolateEmPo;
    private RawMaterial ovos;
    private RawMaterial leite;

    @BeforeEach
    void setUp() {
        // Criar produtos baseados no requests.http
        boloDeChocolate = new Product();
        boloDeChocolate.setId(1L);
        boloDeChocolate.setName("Bolo de Chocolate");
        boloDeChocolate.setSku("BOLO-CHOC-001");
        boloDeChocolate.setSalePrice(new BigDecimal("35.00"));

        paoFrances = new Product();
        paoFrances.setId(2L);
        paoFrances.setName("Pão Francês");
        paoFrances.setSku("PAO-FR-001");
        paoFrances.setSalePrice(new BigDecimal("1.00"));

        // Criar matérias-primas baseadas no requests.http
        farinha = new RawMaterial();
        farinha.setId(1L);
        farinha.setName("Farinha de Trigo");
        farinha.setCode("FARINHA-001");
        farinha.setStockQuantity(1000);

        acucar = new RawMaterial();
        acucar.setId(2L);
        acucar.setName("Açúcar");
        acucar.setCode("ACUCAR-001");
        acucar.setStockQuantity(500);

        chocolateEmPo = new RawMaterial();
        chocolateEmPo.setId(3L);
        chocolateEmPo.setName("Chocolate em Pó");
        chocolateEmPo.setCode("CHOC-PO-001");
        chocolateEmPo.setStockQuantity(300);

        ovos = new RawMaterial();
        ovos.setId(4L);
        ovos.setName("Ovos");
        ovos.setCode("OVOS-001");
        ovos.setStockQuantity(200);

        leite = new RawMaterial();
        leite.setId(5L);
        leite.setName("Leite");
        leite.setCode("LEITE-001");
        leite.setStockQuantity(500);
    }

    @Test
    void shouldCalculateProductionSuggestionCorrectly() {

        ProductRawMaterial boloFarinha = new ProductRawMaterial();
        boloFarinha.setProduct(boloDeChocolate);
        boloFarinha.setRawMaterial(farinha);
        boloFarinha.setRequiredQuantity(500);

        ProductRawMaterial boloAcucar = new ProductRawMaterial();
        boloAcucar.setProduct(boloDeChocolate);
        boloAcucar.setRawMaterial(acucar);
        boloAcucar.setRequiredQuantity(200);

        ProductRawMaterial boloChocolate = new ProductRawMaterial();
        boloChocolate.setProduct(boloDeChocolate);
        boloChocolate.setRawMaterial(chocolateEmPo);
        boloChocolate.setRequiredQuantity(150);

        when(productRepository.findAll()).thenReturn(Collections.singletonList(boloDeChocolate));
        when(productRawMaterialRepository.findByProduct_Id(1L))
                .thenReturn(Arrays.asList(boloFarinha, boloAcucar, boloChocolate));

        // Act
        ProductionSuggestionResponse result = productionService.calculateProductionSuggestion();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.suggestedProducts().size());
        
        var suggestion = result.suggestedProducts().get(0);
        assertEquals("Bolo de Chocolate", suggestion.productName());
        assertEquals(2, suggestion.maxQuantity()); // Limitado por farinha: 1000/500 = 2
        assertEquals(new BigDecimal("35.00"), suggestion.unitPrice());
        assertEquals(new BigDecimal("70.00"), suggestion.totalValue()); // 2 * 35
        
        assertEquals(new BigDecimal("70.00"), result.totalValue());
    }

    @Test
    void shouldPrioritizeProductsByHighestValue() {
        // Arrange - Ambos produtos podem ser feitos, mas bolo tem maior valor
        ProductRawMaterial boloFarinha = new ProductRawMaterial();
        boloFarinha.setProduct(boloDeChocolate);
        boloFarinha.setRawMaterial(farinha);
        boloFarinha.setRequiredQuantity(500);

        ProductRawMaterial paoFarinha = new ProductRawMaterial();
        paoFarinha.setProduct(paoFrances);
        paoFarinha.setRawMaterial(farinha);
        paoFarinha.setRequiredQuantity(100);

        when(productRepository.findAll()).thenReturn(Arrays.asList(paoFrances, boloDeChocolate));
        when(productRawMaterialRepository.findByProduct_Id(1L))
                .thenReturn(Collections.singletonList(boloFarinha));
        when(productRawMaterialRepository.findByProduct_Id(2L))
                .thenReturn(Collections.singletonList(paoFarinha));

        // Act
        ProductionSuggestionResponse result = productionService.calculateProductionSuggestion();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.suggestedProducts().size());
        
        // Primeiro produto deve ser Bolo de Chocolate (maior valor total)
        assertEquals("Bolo de Chocolate", result.suggestedProducts().get(0).productName());
        assertEquals(new BigDecimal("70.00"), result.suggestedProducts().get(0).totalValue());
        
        // Segundo deve ser Pão Francês
        assertEquals("Pão Francês", result.suggestedProducts().get(1).productName());
        assertEquals(new BigDecimal("10.00"), result.suggestedProducts().get(1).totalValue());
    }

    @Test
    void shouldReturnZeroWhenNoRawMaterialsAvailable() {
        // Arrange - Produto sem matérias-primas
        when(productRepository.findAll()).thenReturn(Collections.singletonList(boloDeChocolate));
        when(productRawMaterialRepository.findByProduct_Id(1L))
                .thenReturn(Collections.emptyList());

        // Act
        ProductionSuggestionResponse result = productionService.calculateProductionSuggestion();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.suggestedProducts().size());
        assertEquals(BigDecimal.ZERO, result.totalValue());
    }

    @Test
    void shouldCalculateCorrectlyWhenLimitedByOneRawMaterial() {
        // Arrange - Limitado pelo chocolate (300/150 = 2), mesmo que farinha permita mais
        ProductRawMaterial boloFarinha = new ProductRawMaterial();
        boloFarinha.setProduct(boloDeChocolate);
        boloFarinha.setRawMaterial(farinha);
        boloFarinha.setRequiredQuantity(100); // Permitiria 10 bolos

        ProductRawMaterial boloChocolate = new ProductRawMaterial();
        boloChocolate.setProduct(boloDeChocolate);
        boloChocolate.setRawMaterial(chocolateEmPo);
        boloChocolate.setRequiredQuantity(150); // Limita a 2 bolos

        when(productRepository.findAll()).thenReturn(Collections.singletonList(boloDeChocolate));
        when(productRawMaterialRepository.findByProduct_Id(1L))
                .thenReturn(Arrays.asList(boloFarinha, boloChocolate));

        // Act
        ProductionSuggestionResponse result = productionService.calculateProductionSuggestion();

        // Assert
        var suggestion = result.suggestedProducts().get(0);
        assertEquals(2, suggestion.maxQuantity()); // Limitado pelo chocolate, não pela farinha
    }

    @Test
    void shouldReturnMessageWhenNoProductsCanBeProduced() {
        // Arrange
        RawMaterial farinhaVazia = new RawMaterial();
        farinhaVazia.setId(1L);
        farinhaVazia.setName("Farinha de Trigo");
        farinhaVazia.setStockQuantity(0); // Sem estoque

        ProductRawMaterial boloFarinha = new ProductRawMaterial();
        boloFarinha.setProduct(boloDeChocolate);
        boloFarinha.setRawMaterial(farinhaVazia);
        boloFarinha.setRequiredQuantity(500);

        when(productRepository.findAll()).thenReturn(Collections.singletonList(boloDeChocolate));
        when(productRawMaterialRepository.findByProduct_Id(1L))
                .thenReturn(Collections.singletonList(boloFarinha));

        // Act
        ProductionSuggestionResponse result = productionService.calculateProductionSuggestion();

        // Assert
        assertEquals(0, result.suggestedProducts().size());
        assertTrue(result.message().contains("Nenhum produto"));
    }

    @Test
    void shouldCalculateWithAllRawMaterialsFromRequestsHttp() {
        // Arrange - Testar com TODAS as matérias-primas do requests.http
        // Bolo precisa de: 500g farinha, 200g açúcar, 150g chocolate, 6 ovos, 250ml leite
        ProductRawMaterial boloFarinha = new ProductRawMaterial();
        boloFarinha.setProduct(boloDeChocolate);
        boloFarinha.setRawMaterial(farinha);
        boloFarinha.setRequiredQuantity(500);

        ProductRawMaterial boloAcucar = new ProductRawMaterial();
        boloAcucar.setProduct(boloDeChocolate);
        boloAcucar.setRawMaterial(acucar);
        boloAcucar.setRequiredQuantity(200);

        ProductRawMaterial boloChocolate = new ProductRawMaterial();
        boloChocolate.setProduct(boloDeChocolate);
        boloChocolate.setRawMaterial(chocolateEmPo);
        boloChocolate.setRequiredQuantity(150);

        ProductRawMaterial boloOvos = new ProductRawMaterial();
        boloOvos.setProduct(boloDeChocolate);
        boloOvos.setRawMaterial(ovos);
        boloOvos.setRequiredQuantity(6);

        ProductRawMaterial boloLeite = new ProductRawMaterial();
        boloLeite.setProduct(boloDeChocolate);
        boloLeite.setRawMaterial(leite);
        boloLeite.setRequiredQuantity(250);

        when(productRepository.findAll()).thenReturn(Collections.singletonList(boloDeChocolate));
        when(productRawMaterialRepository.findByProduct_Id(1L))
                .thenReturn(Arrays.asList(boloFarinha, boloAcucar, boloChocolate, boloOvos, boloLeite));

        // Act
        ProductionSuggestionResponse result = productionService.calculateProductionSuggestion();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.suggestedProducts().size());
        
        var suggestion = result.suggestedProducts().get(0);
        assertEquals("Bolo de Chocolate", suggestion.productName());
        assertEquals(2, suggestion.maxQuantity()); // Limitado por: farinha(2), açúcar(2), chocolate(2), leite(2), ovos(33)
        assertEquals(new BigDecimal("70.00"), suggestion.totalValue());
    }
}
