package com.controleestoque.backend.service;

import com.controleestoque.backend.dto.RawMaterialRequest;
import com.controleestoque.backend.entity.RawMaterial;
import com.controleestoque.backend.repository.RawMaterialRepository;
import com.controleestoque.backend.repository.ProductRawMaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RawMaterialService {

    private final RawMaterialRepository repository;
    private final ProductRawMaterialRepository productRawMaterialRepository;

    public RawMaterialService(RawMaterialRepository repository,
                             ProductRawMaterialRepository productRawMaterialRepository) {
        this.repository = repository;
        this.productRawMaterialRepository = productRawMaterialRepository;
    }

    // Criar matéria-prima
    public RawMaterial create(RawMaterialRequest request) {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setName(request.name());
        rawMaterial.setCode(request.code());
        rawMaterial.setStockQuantity(request.stockQuantity());

        return repository.save(rawMaterial);
    }

    // Listar todas
    public List<RawMaterial> findAll() {
        return repository.findAll();
    }

    // Buscar por ID
    public RawMaterial findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Raw material not found"));
    }

    // Atualizar
    public RawMaterial update(Long id, RawMaterialRequest request) {
        RawMaterial rawMaterial = findById(id);

        rawMaterial.setName(request.name());
        rawMaterial.setCode(request.code());
        rawMaterial.setStockQuantity(request.stockQuantity());

        return repository.save(rawMaterial);
    }

    /**
     * Deleta uma matéria-prima e TODAS as suas associações com produtos
     * em cascata (resolve o erro de constraint de chave estrangeira)
     */
    @Transactional
    public void deleteById(Long id) {
        // Verifica se existe
        RawMaterial existing = findById(id);
        
        // Deletar todas as associações desta matéria-prima
        productRawMaterialRepository.deleteByRawMaterial_Id(id);
        
        // Deletar a matéria-prima
        repository.delete(existing);
    }

}
