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
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * DTO de entrada (Record) para criação/atualização de Pedido.
 */
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
        @NotNull(message = "valorTotal é obrigatório") @Positive(message = "valorTotal deve ser maior que zero") BigDecimal valorTotal,

        // ---- Campos auxiliares para criação automática de transação quando situacaoPedido=FATURADO ----
        @Schema(description = "Data de vencimento da transação gerada (apenas se FATURADO)", example = "2025-09-30")
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dataVencimentoTransacao,
        @Schema(description = "Indicador se a transação já está paga (apenas se FATURADO)") Boolean pagoTransacao,
        @Schema(description = "Data de pagamento da transação (apenas se pagoTransacao=true)", example = "2025-09-30")
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dataPagamentoTransacao
) {
}
