package com.controleestoque.backend.service;

import com.controleestoque.backend.dto.ProductRequest;
import com.controleestoque.backend.entity.Product;
import com.controleestoque.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private com.controleestoque.backend.repository.ProductRawMaterialRepository productRawMaterialRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequest validProductRequest;
    private Product validProduct;

    @BeforeEach
    void setUp() {
        validProductRequest = new ProductRequest(
                "Bolo de Chocolate",
                "BOLO-CHOC-001",
                "Bolo de chocolate 1kg",
                0,
                5,
                new BigDecimal("15.00"),
                new BigDecimal("35.00")
        );

        validProduct = new Product();
        validProduct.setId(1L);
        validProduct.setName("Bolo de Chocolate");
        validProduct.setSku("BOLO-CHOC-001");
        validProduct.setDescription("Bolo de chocolate 1kg");
        validProduct.setQuantity(0);
        validProduct.setMinQuantity(5);
        validProduct.setCostPrice(new BigDecimal("15.00"));
        validProduct.setSalePrice(new BigDecimal("35.00"));
    }

    @Test
    void shouldCreateProductSuccessfully() {
        // Arrange
        when(productRepository.existsBySku(validProductRequest.sku())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(validProduct);

        // Act
        Product result = productService.create(validProductRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Bolo de Chocolate", result.getName());
        assertEquals("BOLO-CHOC-001", result.getSku());
        assertEquals(new BigDecimal("35.00"), result.getSalePrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenSkuAlreadyExists() {
        // Arrange
        when(productRepository.existsBySku(validProductRequest.sku())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.create(validProductRequest)
        );

        assertEquals("SKU já existe.", exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldFindProductById() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(validProduct));

        // Act
        Product result = productService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Bolo de Chocolate", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.findById(999L)
        );

        assertTrue(exception.getMessage().contains("não encontrado"));
    }

    @Test
    void shouldDeleteProductAndAssociations() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(validProduct));
        doNothing().when(productRawMaterialRepository).deleteByProduct_Id(1L);
        doNothing().when(productRepository).delete(validProduct);

        // Act
        productService.delete(1L);

        // Assert
        verify(productRawMaterialRepository, times(1)).deleteByProduct_Id(1L);
        verify(productRepository, times(1)).delete(validProduct);
    }
}
