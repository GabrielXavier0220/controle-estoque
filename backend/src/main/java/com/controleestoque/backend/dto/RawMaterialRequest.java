package com.controleestoque.backend.dto;

public record RawMaterialRequest(
        String name,
        String code,
        Integer stockQuantity
) {
}
