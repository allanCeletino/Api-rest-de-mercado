package com.mercado_api.service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mercado_api.Enum.StatusPedido;
import com.mercado_api.Repository.ItemPedidoRepository;
import com.mercado_api.Repository.PedidoRepository;
import com.mercado_api.Repository.ProdutoRepository;
import com.mercado_api.Repository.UsuarioRepository;
import com.mercado_api.dto.ItemCarrinhoDTO;
import com.mercado_api.dto.PedidoDTO;
import com.mercado_api.model.ItemPedido;
import com.mercado_api.model.Pedido;
import com.mercado_api.model.Produto;
import com.mercado_api.model.Usuario;

import jakarta.transaction.Transactional;


@Service
public class PedidoService {
    @Autowired
    private PedidoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Vamos precisar dele para achar o cliente!

    @Autowired
    private ProdutoRepository produtoRepository; // Vamos precisar dele para ver o preço do produto!

    @Autowired
    private ItemPedidoRepository itemPedidoRepository; // Vamos precisar dele para salvar os itens!

    @Autowired
    private ProdutoService produtoService; // Vamos usar o método de dar baixa no estoque que você já fez!

    //page findByDataPedidoBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable); OK
    //paga findByStatusPedido(StatusPedido statusPedido, Pageable pageable);OK
    //Page<Pedido> findByUsuario(Usuario usuario, Pageable pageable); // procupa pro ususario OK
    //@Query("SELECT p FROM Pedido p JOIN FETCH p.usuario WHERE p.id = :id")
    //Optional<Pedido> findByIdComUsuario(@Param("id") Long id);
    //soma todos os preo do todos os produto 
    //um cliete tem que tem para exiti um predido

    //metodo para fortEnt para carrgas inumetros estado de pedido
    public Page<Pedido> buscarPorStatusPedido(StatusPedido statusPedido, Pageable pageable){
        return repository.findByStatusPedido(statusPedido, pageable);
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return repository.findByIdComUsuario(id);
    }

    //metodo para o fortennt para motras todos os usuarios
    public Page<Pedido> buscarPorUsuario(Usuario usuario, Pageable pegeable){
        return repository.findByUsuario(usuario, pegeable);
    }
    
    //metodos para motras todas as datadepedido e coloca uma regra de nao exitir data da fim meno que a data iniciar
    public Page<Pedido> BuscarPorDataPorPedido(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pegeable){
        if(dataFim.isBefore(dataInicio)){
            throw new RuntimeException("Erro:periodo nao exitir");//tratamento de erro
        }
        return repository.findByDataPedidoBetween(dataInicio, dataFim, pegeable);
    }

@Transactional
public Pedido criarPedido(PedidoDTO sacola, Usuario usuarioLogado) {

    if (sacola.getItens() == null || sacola.getItens().isEmpty()) {
        throw new RuntimeException("Carrinho vazio não pode gerar pedido.");
    }

    Pedido novoPedido = new Pedido();
    novoPedido.setUsuario(usuarioLogado);
    novoPedido.setStatusPedido(StatusPedido.PENDENTE);
    novoPedido.setTotal(BigDecimal.ZERO);

    novoPedido = repository.saveAndFlush(novoPedido);

    BigDecimal totalPedido = BigDecimal.ZERO;
    
    // 🟢 1. Criamos uma lista na memória para guardar os itens do retorno
    java.util.List<ItemPedido> listaItensDoPedido = new java.util.ArrayList<>();

    for (ItemCarrinhoDTO itemSacola : sacola.getItens()) {

        Produto produtoReal = produtoRepository.findById(itemSacola.getProdutoId())
                .orElseThrow(() -> new RuntimeException(
                        "Produto não encontrado: " + itemSacola.getProdutoId()
                ));

        ItemPedido linhaItem = new ItemPedido();
        linhaItem.setPedido(novoPedido); 
        linhaItem.setProduto(produtoReal);
        linhaItem.setQuantidade(itemSacola.getQuantidade());
        linhaItem.setPrecoUnitario(produtoReal.getPreco());
        
        BigDecimal subtotal = produtoReal.getPreco()
                .multiply(BigDecimal.valueOf(itemSacola.getQuantidade()));

        totalPedido = totalPedido.add(subtotal);

        // 🟢 2. Salvamos no banco e adicionamos o item na nossa lista da memória
        ItemPedido itemSalvo = itemPedidoRepository.save(linhaItem);
        listaItensDoPedido.add(itemSalvo);

        produtoService.darBaixaEstoque(
                produtoReal.getId(),
                itemSacola.getQuantidade()
        );
    }

    // 🟢 3. Vincula a lista cheia de itens de volta ao novoPedido antes de salvar definitivo
    novoPedido.setItens(listaItensDoPedido);
    novoPedido.setTotal(totalPedido);

    return repository.save(novoPedido);
}

@Transactional
public Pedido atualizarPedido(Pedido pedido) {

    Pedido pedidoExistente = repository.findById(pedido.getId())
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));

    pedidoExistente.setStatusPedido(pedido.getStatusPedido());

    return repository.save(pedidoExistente);
}

@Transactional
public void deletarPedido(Long id) {

    if (!repository.existsById(id)) {
        throw new RuntimeException("Pedido não encontrado.");
    }

    repository.deleteById(id);
}

}


