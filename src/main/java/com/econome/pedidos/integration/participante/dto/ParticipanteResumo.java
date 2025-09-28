package com.econome.pedidos.integration.participante.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO resumido de Participante para ser embutido em respostas de Pedido.
 * Mantém somente campos necessários à UI para exibição.
 */
@Schema(name = "ParticipanteResumo")
public record ParticipanteResumo(
        Long id,
        String codigo,
        String nome,
        String cpfCnpj,
        String tipoPessoa,
        String tipoParticipante
) {
}
