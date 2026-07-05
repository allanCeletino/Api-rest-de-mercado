package com.mercado_api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="Produto")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=100)
    @NotBlank(message="O nome é obrigatório")
    @Size( min=3, max=100,  message="O nome é obrigatório é tem que tem no minimo de 3 e maximo de 100 carateri")
    private String nome;

    @Column(nullable=false)
    @NotNull(message="O preço é obrigatório")
    @PositiveOrZero(message="O preço não pode ser negativo")
    private BigDecimal preco;

    @Column(nullable=false)
    @NotNull(message="A quantidade é obrigatória")
    @Min(value=0, message="a quantidade nao pode ser zero")
    private long quantidade;
    
}
