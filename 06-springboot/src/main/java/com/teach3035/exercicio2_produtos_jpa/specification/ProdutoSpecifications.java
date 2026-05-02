package com.teach3035.exercicio2_produtos_jpa.specification;

import com.teach3035.exercicio2_produtos_jpa.persistence.ProdutoEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProdutoSpecifications {

    public static Specification<ProdutoEntity> porNome(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) {
                return null;
            }
            return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }

    public static Specification<ProdutoEntity> criadoEntre(LocalDateTime inicio, LocalDateTime fim) {
        return (root, query, cb) -> {
            if (inicio == null && fim == null) {
                return null;
            }
            if (inicio != null && fim != null) {
                return cb.between(root.get("dataCriacao"), inicio, fim);
            }
            if (inicio != null) {
                return cb.greaterThanOrEqualTo(root.get("dataCriacao"), inicio);
            }
            return cb.lessThanOrEqualTo(root.get("dataCriacao"), fim);
        };
    }

    public static Specification<ProdutoEntity> precoEntre(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min != null && max != null) {
                return cb.between(root.get("preco"), min, max);
            }
            if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("preco"), min);
            }
            return cb.lessThanOrEqualTo(root.get("preco"), max);
        };
    }

    public static Specification<ProdutoEntity> ativo() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }
}

