package com.mercado_api.Repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mercado_api.model.ItemPedido;
import com.mercado_api.model.Pedido;


@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long>{
    
  
   
    //procupar por id e pedido
    @Query("SELECT p FROM ItemPedido p JOIN FETCH p.pedido WHERE p.id = :id ")
    Optional<ItemPedido> findByIdComPedido(@Param("id") Long id);
    // Quando você rodar esse método, o preço unitário de cada um já vem preenchido!
    List<ItemPedido> findByPedido(Pedido pedido);
 


    
}
