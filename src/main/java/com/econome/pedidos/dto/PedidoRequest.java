package com.econome.pedidos.dto;

import com.econome.pedidos.enums.SituacaoPedido;
import com.econome.pedidos.enums.TipoPedido;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PedidoRequest", description = "Dados de entrada para criação/atualização de pedidos")
public record PedidoRequest(
        @Schema(description = "Data/hora de emissão do pedido (com timezone)", example = "2025-09-24T10:15:30.000-03:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") ZonedDateTime dataEmissaoPedido,
        @Schema(description = "Número único do pedido", maxLength = 100, example = "PED-123")
        @NotBlank(message = "numeroPedido é obrigatório") @Size(max = 100, message = "numeroPedido deve ter no máximo 100 caracteres") String numeroPedido,
        @Schema(description = "Tipo do pedido (ENTRADA ou SAIDA)")
        @NotNull(message = "tipoPedido é obrigatório") TipoPedido tipoPedido,
        @Schema(description = "Situação atual do pedido (PENDENTE, FATURADO, CANCELADO)")
        @NotNull(message = "situacaoPedido é obrigatório") SituacaoPedido situacaoPedido,
        @Schema(description = "Valor total do pedido (> 0)", example = "150.00")
        @NotNull(message = "valorTotal é obrigatório") @Positive(message = "valorTotal deve ser maior que zero") BigDecimal valorTotal
) {
}
