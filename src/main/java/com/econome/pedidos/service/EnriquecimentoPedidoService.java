package com.econome.pedidos.service;

import com.econome.pedidos.dto.PedidoResponse;
import com.econome.pedidos.integration.participante.client.ParticipantesClient;
import com.econome.pedidos.integration.participante.dto.ParticipanteResumo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Serviço responsável por enriquecer pedidos com o participante embutido.
 * Implementação simples (N chamadas). Pode ser otimizada para batch quando endpoint existir.
 */
@Service
@RequiredArgsConstructor
public class EnriquecimentoPedidoService {

    private final ParticipantesClient participantesClient;

    public List<PedidoResponse> enriquecer(List<PedidoResponse> pedidos) {
        // Coleta IDs distintos
        List<Long> ids = pedidos.stream()
                .map(PedidoResponse::participanteId)
                .filter(id -> id != null && id > 0)
                .distinct()
                .toList();

        Map<Long, ParticipanteResumo> participantes = ids.stream()
                .map(id -> participantesClient.buscarPorId(id).orElse(null))
                .filter(p -> p != null)
                .collect(Collectors.toMap(ParticipanteResumo::id, p -> p));

        return pedidos.stream()
                .map(p -> participantes.containsKey(p.participanteId())
                        ? new PedidoResponse(p.id(), p.dataEmissaoPedido(), p.numeroPedido(), p.tipoPedido(), p.situacaoPedido(), p.valorTotal(), p.participanteId(), participantes.get(p.participanteId()))
                        : p)
                .toList();
    }

    public PedidoResponse enriquecer(PedidoResponse pedido) {
        if (pedido.participanteId() == null) return pedido;
        return participantesClient.buscarPorId(pedido.participanteId())
                .map(participante -> new PedidoResponse(pedido.id(), pedido.dataEmissaoPedido(), pedido.numeroPedido(), pedido.tipoPedido(), pedido.situacaoPedido(), pedido.valorTotal(), pedido.participanteId(), participante))
                .orElse(pedido);
    }
}
