package com.mercado_api.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
@Entity
@Table(name="itens_pedido") // Mudamos para o padrão snake_case em minúsculo
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message="O pedido é obrigatório")
    @JoinColumn(name = "pedido_id", nullable = false) 
    @JsonBackReference
    private Pedido pedido;

    @ManyToOne
    @NotNull(message="O produto é obrigatório")
    @JoinColumn(name = "produtoId", nullable = false) // Corrigido o nome da coluna para minúsculo
    private Produto produto; 

    @NotNull(message="A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser no mínimo 1") // 🔥 Garante que ninguém compre 0 itens
    @Column(nullable = false)
    private Long quantidade;

    @NotNull(message="O preço unitário é obrigatório")
    @Column(name = "preco_unitario", nullable = false) // Mapeia o nome correto no banco
    private BigDecimal precoUnitario;

    //
}