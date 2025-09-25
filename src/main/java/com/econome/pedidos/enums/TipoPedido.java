package com.econome.pedidos.enums;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Enum que representa o tipo do Pedido (entrada de itens ou saída).
 */
@Schema(description = "Tipo do pedido")
public enum TipoPedido {
    ENTRADA, SAIDA
}
