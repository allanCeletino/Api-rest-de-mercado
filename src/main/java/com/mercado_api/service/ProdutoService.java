package com.mercado_api.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mercado_api.Repository.ProdutoRepository;
import com.mercado_api.model.Produto;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    // 1. CADASTRAR PRODUTO
    @Transactional
    public Produto cadastrarProduto(Produto produto) {

        if (produto == null) {
            throw new RuntimeException("Produto não pode ser nulo.");
        }

        if (produto.getPreco() == null) {
            throw new RuntimeException("O preço do produto é obrigatório.");
        }

        if (produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("O preço do produto não pode ser negativo.");
        }

        if (produto.getQuantidade() < 0) {
            throw new RuntimeException("A quantidade não pode ser negativa.");
        }

        return repository.save(produto);
    }

    // 2. DAR BAIXA NO ESTOQUE
    @Transactional
    public void darBaixaEstoque(Long produtoId, long quantidadeVendida) {

        if (produtoId == null) {
            throw new RuntimeException("O id do produto é obrigatório.");
        }

        if (quantidadeVendida <= 0) {
            throw new RuntimeException("A quantidade vendida deve ser maior que zero.");
        }

        Produto produto = repository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));

        if (produto.getQuantidade() < quantidadeVendida) {
            throw new RuntimeException(
                    "Estoque insuficiente para o produto: " + produto.getNome());
        }

        produto.setQuantidade(produto.getQuantidade() - quantidadeVendida);

        repository.save(produto);
    }

    // 3. BUSCAR PRODUTO POR NOME
    public Page<Produto> buscarPorNome(String nome, Pageable pageable) {
        return repository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    // 4. APLICAR AUMENTO EM MASSA
    @Transactional
    public void aplicarAumentoEmMassa(Double percentual) {

        if (percentual == null) {
            throw new RuntimeException("O percentual é obrigatório.");
        }

        if (percentual < 0) {
            throw new RuntimeException("O percentual não pode ser negativo.");
        }

        Double multiplicador = 1 + (percentual / 100);

        repository.aplicarAumentoEmMassa(multiplicador);
    }

    // 5. BUSCAR POR ID
    public Optional<Produto> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // 6. BUSCAR PRODUTOS POR INTERVALO DE ID
    public Page<Produto> buscarPorIntervaloDeId(Long idInicial,
                                                Long idFinal,
                                                Pageable pageable) {

        if (idInicial == null || idFinal == null) {
            throw new RuntimeException("Os IDs são obrigatórios.");
        }

        if (idInicial > idFinal) {
            throw new RuntimeException("O ID inicial não pode ser maior que o ID final.");
        }

        return repository.findByIdBetween(idInicial, idFinal, pageable);
    }

}