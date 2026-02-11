package com.controleestoque.backend.repository;

import com.controleestoque.backend.entity.ProductRawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRawMaterialRepository extends JpaRepository<ProductRawMaterial, Long> {

    List<ProductRawMaterial> findByProduct_Id(Long productId);

    Optional<ProductRawMaterial> findByProduct_IdAndRawMaterial_Id(Long productId, Long rawMaterialId);
    
    /**
     * Deleta todas as associações de um produto específico
     * Usado para deleção em cascata quando um produto é removido
     */
    void deleteByProduct_Id(Long productId);
    
    /**
     * Deleta todas as associações de uma matéria-prima específica
     * Usado para deleção em cascata quando uma matéria-prima é removida
     */
    void deleteByRawMaterial_Id(Long rawMaterialId);
}
