package com.controleestoque.backend.service;

import com.controleestoque.backend.dto.ProductRawMaterialRequest;
import com.controleestoque.backend.entity.Product;
import com.controleestoque.backend.entity.ProductRawMaterial;
import com.controleestoque.backend.entity.RawMaterial;
import com.controleestoque.backend.repository.ProductRawMaterialRepository;
import com.controleestoque.backend.repository.ProductRepository;
import com.controleestoque.backend.repository.RawMaterialRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductRawMaterialService {

    private final ProductRawMaterialRepository productRawMaterialRepository;
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public ProductRawMaterialService(
            ProductRawMaterialRepository productRawMaterialRepository,
            ProductRepository productRepository,
            RawMaterialRepository rawMaterialRepository
    ) {
        this.productRawMaterialRepository = productRawMaterialRepository;
        this.productRepository = productRepository;
        this.rawMaterialRepository = rawMaterialRepository;
    }


    @Transactional
    public ProductRawMaterial addRawMaterialToProduct(Long productId, ProductRawMaterialRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        RawMaterial rawMaterial = rawMaterialRepository.findById(request.rawMaterialId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Raw material not found"));

        // se já existir associação, atualiza requiredQuantity
        ProductRawMaterial prm = productRawMaterialRepository
                .findByProduct_IdAndRawMaterial_Id(productId, request.rawMaterialId())
                .orElseGet(ProductRawMaterial::new);

        prm.setProduct(product);
        prm.setRawMaterial(rawMaterial);
        prm.setRequiredQuantity(request.requiredQuantity());

        return productRawMaterialRepository.save(prm);
    }

    // Listar matérias-primas associadas a um produto
    public List<ProductRawMaterial> listByProductId(Long productId) {
        return productRawMaterialRepository.findByProduct_Id(productId);
    }

    // Atualizar requiredQuantity pelo id da associação
    @Transactional
    public ProductRawMaterial updateRequiredQuantity(Long associationId, ProductRawMaterialRequest request) {
        ProductRawMaterial prm = productRawMaterialRepository.findById(associationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Association not found"));

        prm.setRequiredQuantity(request.requiredQuantity());
        return productRawMaterialRepository.save(prm);
    }

    // DELETE por id da associação
    public void deleteById(Long id) {
        if (!productRawMaterialRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Association not found");
        }
        productRawMaterialRepository.deleteById(id);
    }

    // DELETE por (productId + rawMaterialId)
    public void deleteAssociation(Long productId, Long rawMaterialId) {
        ProductRawMaterial prm = productRawMaterialRepository
                .findByProduct_IdAndRawMaterial_Id(productId, rawMaterialId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Association not found"));

        productRawMaterialRepository.delete(prm);
    }
}
