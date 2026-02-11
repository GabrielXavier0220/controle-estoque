package com.controleestoque.backend.controller;

import com.controleestoque.backend.dto.RawMaterialRequest;
import com.controleestoque.backend.entity.RawMaterial;
import com.controleestoque.backend.service.RawMaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/raw-materials")
public class RawMaterialController {

    private final RawMaterialService service;

    // O Spring injeta o service automaticamente
    public RawMaterialController(RawMaterialService service) {

        this.service = service;
    }

    // CRIAR matéria-prima
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RawMaterial create(@RequestBody RawMaterialRequest request) {

        return service.create(request);
    }

    // LISTAR todas
    @GetMapping
    public List<RawMaterial> findAll() {

        return service.findAll();
    }

    // BUSCAR por ID
    @GetMapping("/{id}")
    public RawMaterial findById(@PathVariable Long id) {

        return service.findById(id);
    }

    // ATUALIZAR
    @PutMapping("/{id}")
    public RawMaterial update(@PathVariable Long id, @RequestBody RawMaterialRequest request) {
        return service.update(id, request);
    }

    // DELETAR
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        service.deleteById(id);
    }


}

