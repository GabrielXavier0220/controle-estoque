package com.controleestoque.backend.controller;

import com.controleestoque.backend.dto.ProductRawMaterialRequest;
import com.controleestoque.backend.entity.ProductRawMaterial;
import com.controleestoque.backend.service.ProductRawMaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductRawMaterialController {

    private final ProductRawMaterialService service;

    public ProductRawMaterialController(ProductRawMaterialService service) {
        this.service = service;
    }

    // Criar associação: produto -> matéria-prima (com requiredQuantity)
    @PostMapping("/products/{productId}/raw-materials")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductRawMaterial addRawMaterialToProduct(
            @PathVariable Long productId,
            @RequestBody ProductRawMaterialRequest request
    ) {
        return service.addRawMaterialToProduct(productId, request);
    }

    // Listar matérias-primas associadas a um produto
    @GetMapping("/products/{productId}/raw-materials")
    public List<ProductRawMaterial> listByProductId(@PathVariable Long productId) {
        return service.listByProductId(productId);
    }

    // Atualizar apenas a requiredQuantity da associação
    @PutMapping("/product-raw-materials/{associationId}")
    public ProductRawMaterial updateRequiredQuantity(
            @PathVariable Long associationId,
            @RequestBody ProductRawMaterialRequest request
    ) {
        return service.updateRequiredQuantity(associationId, request);
    }

    // Deletar associação pelo id
    @DeleteMapping("/product-raw-materials/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    // Deletar associação por productId + rawMaterialId
    @DeleteMapping("/products/{productId}/raw-materials/{rawMaterialId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssociation(@PathVariable Long productId, @PathVariable Long rawMaterialId) {
        service.deleteAssociation(productId, rawMaterialId);
    }
}
