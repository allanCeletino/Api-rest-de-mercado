package com.mercado_api.dto;
 

import java.util.ArrayList;
import java.util.List;


import lombok.Data;

@Data
public class PedidoDTO {
    private Long id;
    private List<ItemCarrinhoDTO> itens = new ArrayList<>();
}
