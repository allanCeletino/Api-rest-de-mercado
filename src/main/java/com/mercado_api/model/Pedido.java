package com.mercado_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mercado_api.Enum.StatusPedido;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Pedidos")
public class Pedido {
 @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@Column(nullable = false)
@Enumerated(EnumType.STRING)
private StatusPedido statusPedido;

@Column(name = "data_pedido", nullable = false)
private LocalDateTime dataPedido;

@ManyToOne
@JoinColumn(name = "usuario_id", nullable = false) // 🟢 Apenas o usuário fica aqui!
private Usuario usuario;

@OneToMany(mappedBy = "pedido", cascade  = CascadeType.ALL, orphanRemoval = true)
@JsonManagedReference
private List<ItemPedido> itens;

@PrePersist
protected void onCreate() {
    this.dataPedido = LocalDateTime.now();
}
private BigDecimal total;


    
}
