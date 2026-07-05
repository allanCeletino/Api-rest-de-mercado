package com.mercado_api.Repository;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.mercado_api.model.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mercado_api.Enum.StatusPedido;
import com.mercado_api.model.Usuario;


@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    //usuario produto dataPedido statuspedido id

   @Query("SELECT p FROM Pedido p JOIN FETCH p.usuario WHERE p.id = :id")
    Optional<Pedido> findByIdComUsuario(@Param("id") Long id);
    
   Page<Pedido> findByUsuario(Usuario usuario, Pageable pageable); // procupa pro ususario

   //  O JEITO MAIS USADO NO MERCADO: Busca pedidos entre duas datas (Ex: Vendas de hoje)
   Page<Pedido> findByDataPedidoBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable); 
   Page<Pedido> findByStatusPedido(StatusPedido statusPedido, Pageable pageable);

}
