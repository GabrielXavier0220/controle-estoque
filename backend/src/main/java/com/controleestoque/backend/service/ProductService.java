package com.controleestoque.backend.service;

import com.controleestoque.backend.dto.ProductRequest;
import com.controleestoque.backend.entity.Product;
import com.controleestoque.backend.repository.ProductRepository;
import com.controleestoque.backend.repository.ProductRawMaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductRawMaterialRepository productRawMaterialRepository;

    public ProductService(ProductRepository repository, 
                         ProductRawMaterialRepository productRawMaterialRepository) {
        this.repository = repository;
        this.productRawMaterialRepository = productRawMaterialRepository;
    }

    public Product create(ProductRequest request) {

        if (repository.existsBySku(request.sku())) {
            throw new IllegalArgumentException("SKU já existe.");
        }

        Product product = new Product();
        product.setName(request.name());
        product.setSku(request.sku());
        product.setDescription(request.description());
        product.setQuantity(request.quantity());
        product.setMinQuantity(request.minQuantity());
        product.setCostPrice(request.costPrice());
        product.setSalePrice(request.salePrice());

        return repository.save(product);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Produto não encontrado: " + id));
    }

    public Product update(Long id, ProductRequest request) {

        Product existing = findById(id);

        if (!existing.getSku().equals(request.sku())
                && repository.existsBySku(request.sku())) {
            throw new IllegalArgumentException("SKU já existe.");
        }

        existing.setName(request.name());
        existing.setSku(request.sku());
        existing.setDescription(request.description());
        existing.setQuantity(request.quantity());
        existing.setMinQuantity(request.minQuantity());
        existing.setCostPrice(request.costPrice());
        existing.setSalePrice(request.salePrice());

        return repository.save(existing);
    }

    /**
     * Deleta um produto e TODAS as suas associações com matérias-primas
     * em cascata (resolve o erro de constraint de chave estrangeira)
     */
    @Transactional
    public void delete(Long id) {
        Product existing = findById(id);
        
        // Deletar todas as associações deste produto
        productRawMaterialRepository.deleteByProduct_Id(id);
        
        // Deletar o produto
        repository.delete(existing);
    }

}
