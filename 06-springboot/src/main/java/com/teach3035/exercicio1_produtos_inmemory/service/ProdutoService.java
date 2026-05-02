package com.teach3035.exercicio1_produtos_inmemory.service;

import com.teach3035.exercicio1_produtos_inmemory.dto.ProdutoPatchDTO;
import com.teach3035.exercicio1_produtos_inmemory.dto.ProdutoRequestDTO;
import com.teach3035.exercicio1_produtos_inmemory.dto.ProdutoResponseDTO;
import com.teach3035.exercicio1_produtos_inmemory.exception.ProdutoNotFoundException;
import com.teach3035.exercicio1_produtos_inmemory.model.Produto;
import com.teach3035.exercicio1_produtos_inmemory.persistence.ProdutoJpaRepository;
import com.teach3035.exercicio1_produtos_inmemory.persistence.ProdutoEntity;
import com.teach3035.exercicio1_produtos_inmemory.specification.ProdutoSpecifications;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoJpaRepository jpaRepository;

    public ProdutoService(ProdutoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    public List<ProdutoResponseDTO> findAll() {
        return jpaRepository.findAllAtivo().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProdutoResponseDTO> findAllComFiltro(String nome, LocalDateTime dataInicio, LocalDateTime dataFim,
                                                  BigDecimal precoMin, BigDecimal precoMax) {
        Specification<ProdutoEntity> spec = Specification.where(ProdutoSpecifications.ativo());

        if (nome != null) {
            spec = spec.and(ProdutoSpecifications.porNome(nome));
        }
        if (dataInicio != null || dataFim != null) {
            spec = spec.and(ProdutoSpecifications.criadoEntre(dataInicio, dataFim));
        }
        if (precoMin != null || precoMax != null) {
            spec = spec.and(ProdutoSpecifications.precoEntre(precoMin, precoMax));
        }

        return jpaRepository.findAll(spec).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "produtos", key = "#id")
    public ProdutoResponseDTO findById(Long id) {
        ProdutoEntity entity = jpaRepository.findByIdAtivo(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));
        return toResponseDTO(entity);
    }

    @CacheEvict(value = "produtos", allEntries = true)
    public ProdutoResponseDTO create(ProdutoRequestDTO requestDTO) {
        ProdutoEntity entity = new ProdutoEntity();
        entity.setNome(requestDTO.getNome());
        entity.setPreco(requestDTO.getPreco());
        entity = jpaRepository.save(entity);
        return toResponseDTO(entity);
    }

    @CacheEvict(value = "produtos", allEntries = true)
    public ProdutoResponseDTO update(Long id, ProdutoPatchDTO patchDTO) {
        ProdutoEntity entity = jpaRepository.findByIdAtivo(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));

        if (patchDTO.getNome() != null) {
            entity.setNome(patchDTO.getNome());
        }
        if (patchDTO.getPreco() != null) {
            entity.setPreco(patchDTO.getPreco());
        }

        entity = jpaRepository.save(entity);
        return toResponseDTO(entity);
    }

    @CacheEvict(value = "produtos", allEntries = true)
    public void delete(Long id) {
        ProdutoEntity entity = jpaRepository.findByIdAtivo(id)
                .orElseThrow(() -> new ProdutoNotFoundException(id));
        entity.setDeletedAt(LocalDateTime.now());
        jpaRepository.save(entity);
    }

    private ProdutoResponseDTO toResponseDTO(ProdutoEntity entity) {
        return new ProdutoResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getPreco(),
                entity.getDataCriacao(),
                entity.getDataAtualizacao()
        );
    }
}
