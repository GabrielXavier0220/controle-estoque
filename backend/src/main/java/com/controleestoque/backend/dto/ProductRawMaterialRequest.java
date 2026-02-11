package com.controleestoque.backend.dto;

public record ProductRawMaterialRequest(
        Long rawMaterialId,
        Integer requiredQuantity
) {}
