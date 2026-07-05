package com.mercado_api.dto;

import lombok.Data;

@Data
public class ItemCarrinhoDTO {

    private Long produtoId;
    private String nome;
    private Long quantidade;
}