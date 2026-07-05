package com.mercado_api.Repository;

import java.util.Optional;

// 🔥 IMPORTS CORRETOS PARA PAGINAÇÃO DO SPRING:
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mercado_api.model.Produto;
import jakarta.transaction.Transactional;

@Repository // 🔥 Adicionado para o Spring reconhecer a classe
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
     
    // 1. Procurar por ID (Opcional porque pode não encontrar o produto)
    Optional<Produto> findById(Long id);
    
    // 2. Procurar por nome com paginação (Corrigido o "Containing")
    Page<Produto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    // 3. Atualização em lote direto no banco
    @Modifying
    @Transactional
    @Query("UPDATE Produto p SET p.preco = p.preco * :percentual WHERE p.quantidade > 0")
    int aplicarAumentoEmMassa(@Param("percentual") Double percentual);

    // para pode ver por periodos de id

    Page<Produto> findByIdBetween(Long idIniciar, Long idFinal, Pageable pageable);
}