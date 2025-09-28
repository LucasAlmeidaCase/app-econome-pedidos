package com.econome.pedidos.dto;

import com.econome.pedidos.enums.SituacaoPedido;
import com.econome.pedidos.enums.TipoPedido;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.econome.pedidos.integration.participante.dto.ParticipanteResumo;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * DTO de saída (Record) representando um Pedido retornado pela API.
 * Evita expor a entidade JPA diretamente e deixa o contrato claro para consumidores externos.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PedidoResponse", description = "Dados retornados para consultas e operações de pedidos")
public record PedidoResponse(
        @Schema(description = "Identificador único do pedido", example = "1") Long id,
        @Schema(description = "Data/hora de emissão do pedido (com timezone)", example = "2025-09-24T10:15:30.000-03:00")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") ZonedDateTime dataEmissaoPedido,
        @Schema(description = "Número do pedido", example = "PED-123") String numeroPedido,
        @Schema(description = "Tipo do pedido (ENTRADA ou SAIDA)") TipoPedido tipoPedido,
        @Schema(description = "Situação do pedido (PENDENTE, FATURADO, CANCELADO)") SituacaoPedido situacaoPedido,
        @Schema(description = "Valor total do pedido", example = "150.00") BigDecimal valorTotal,
        @Schema(description = "Identificador de participante associado", example = "10") Long participanteId,
        @Schema(description = "Dados resumidos do participante quando embutido")
        @JsonProperty("participante") ParticipanteResumo participanteResumo
) {
}
