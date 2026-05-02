package com.teach3035.exercicio1_produtos_inmemory.controller;

import com.teach3035.exercicio1_produtos_inmemory.dto.ApiResponse;
import com.teach3035.exercicio1_produtos_inmemory.dto.ProdutoPatchDTO;
import com.teach3035.exercicio1_produtos_inmemory.dto.ProdutoRequestDTO;
import com.teach3035.exercicio1_produtos_inmemory.dto.ProdutoResponseDTO;
import com.teach3035.exercicio1_produtos_inmemory.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProdutoResponseDTO>>> findAll() {
        List<ProdutoResponseDTO> produtos = produtoService.findAll();
        return ResponseEntity.ok(ApiResponse.success(produtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> findById(@PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(produto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> create(
            @Valid @RequestBody ProdutoRequestDTO requestDTO) {
        ProdutoResponseDTO produto = produtoService.create(requestDTO);
        URI location = URI.create("/produtos/" + produto.getId());
        return ResponseEntity.created(location).body(ApiResponse.success(produto, "Produto criado com sucesso"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ProdutoResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoPatchDTO patchDTO) {
        ProdutoResponseDTO produto = produtoService.update(id, patchDTO);
        return ResponseEntity.ok(ApiResponse.success(produto, "Produto atualizado com sucesso"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
