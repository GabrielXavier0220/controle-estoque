package com.controleestoque.backend.service;

import com.controleestoque.backend.dto.RawMaterialRequest;
import com.controleestoque.backend.entity.RawMaterial;
import com.controleestoque.backend.repository.ProductRawMaterialRepository;
import com.controleestoque.backend.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceTest {

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @Mock
    private ProductRawMaterialRepository productRawMaterialRepository;

    @InjectMocks
    private RawMaterialService rawMaterialService;

    private RawMaterialRequest validRequest;
    private RawMaterial validRawMaterial;

    @BeforeEach
    void setUp() {
        validRequest = new RawMaterialRequest(
                "Farinha de Trigo",
                "FARINHA-001",
                1000
        );

        validRawMaterial = new RawMaterial();
        validRawMaterial.setId(1L);
        validRawMaterial.setName("Farinha de Trigo");
        validRawMaterial.setCode("FARINHA-001");
        validRawMaterial.setStockQuantity(1000);
    }

    @Test
    void shouldCreateRawMaterialSuccessfully() {
        // Arrange
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(validRawMaterial);

        // Act
        RawMaterial result = rawMaterialService.create(validRequest);

        // Assert
        assertNotNull(result);
        assertEquals("Farinha de Trigo", result.getName());
        assertEquals("FARINHA-001", result.getCode());
        assertEquals(1000, result.getStockQuantity());
        verify(rawMaterialRepository, times(1)).save(any(RawMaterial.class));
    }

    @Test
    void shouldFindAllRawMaterials() {
        // Arrange
        RawMaterial acucar = new RawMaterial();
        acucar.setId(2L);
        acucar.setName("Açúcar");
        acucar.setCode("ACUCAR-001");
        acucar.setStockQuantity(500);

        when(rawMaterialRepository.findAll()).thenReturn(Arrays.asList(validRawMaterial, acucar));

        // Act
        List<RawMaterial> result = rawMaterialService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Farinha de Trigo", result.get(0).getName());
        assertEquals("Açúcar", result.get(1).getName());
    }

    @Test
    void shouldUpdateRawMaterialStockQuantity() {
        // Arrange
        RawMaterialRequest updateRequest = new RawMaterialRequest(
                "Farinha de Trigo",
                "FARINHA-001",
                1500  // Estoque atualizado
        );

        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(validRawMaterial));
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        RawMaterial result = rawMaterialService.update(1L, updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1500, result.getStockQuantity());
        verify(rawMaterialRepository, times(1)).save(any(RawMaterial.class));
    }

    @Test
    void shouldDeleteRawMaterialAndAssociations() {
        // Arrange
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(validRawMaterial));
        doNothing().when(productRawMaterialRepository).deleteByRawMaterial_Id(1L);
        doNothing().when(rawMaterialRepository).delete(validRawMaterial);

        // Act
        rawMaterialService.deleteById(1L);

        // Assert
        verify(productRawMaterialRepository, times(1)).deleteByRawMaterial_Id(1L);
        verify(rawMaterialRepository, times(1)).delete(validRawMaterial);
    }

    @Test
    void shouldThrowExceptionWhenRawMaterialNotFound() {
        // Arrange
        when(rawMaterialRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> rawMaterialService.findById(999L)
        );

        assertTrue(exception.getMessage().contains("not found"));
    }
}
