package com.econome.pedidos.service.impl;

import com.econome.domain.Pedido;
import com.econome.pedidos.dto.PedidoMapper;
import com.econome.pedidos.dto.PedidoRequest;
import com.econome.pedidos.dto.PedidoResponse;
import com.econome.pedidos.exception.PedidoNaoEncontradoException;
import com.econome.pedidos.enums.SituacaoPedido;
import com.econome.pedidos.integration.transacao.event.PedidoCriadoEvent;
import com.econome.pedidos.integration.transacao.event.PedidoAtualizadoEvent;
import com.econome.pedidos.service.PedidoService;
import com.econome.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação do contrato de serviço de Pedidos.
 * Aplica regras transacionais no nível de serviço e delega persistência ao repositório.
 * Utiliza MapStruct para mapeamento entre DTOs e entidade, favorecendo legibilidade e testabilidade.
 * Publica evento de domínio após criação para integração assíncrona com o microserviço de transações
 * (promovendo baixo acoplamento e evitando chamadas HTTP dentro da transação JPA).
 */
@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public PedidoResponse criar(PedidoRequest request) {
        Pedido entity = pedidoMapper.toEntity(request);
        Pedido salvo = pedidoRepository.save(entity);
        eventPublisher.publishEvent(new PedidoCriadoEvent(
                salvo.getId(),
                salvo.getNumeroPedido(),
                salvo.getTipoPedido(),
                salvo.getValorTotal(),
                request.situacaoPedido() == SituacaoPedido.FATURADO,
                request.dataVencimentoTransacao(),
                request.pagoTransacao(),
                request.dataPagamentoTransacao()
        ));
        return pedidoMapper.toResponse(salvo);
    }

    @Override
    @Transactional
    public PedidoResponse atualizar(Long id, PedidoRequest request) {
        Pedido existente = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));

        boolean faturadoAnterior = existente.getSituacaoPedido() == SituacaoPedido.FATURADO;

        pedidoMapper.updateEntityFromRequest(request, existente);
        Pedido atualizado = pedidoRepository.save(existente);

        boolean faturadoAtual = request.situacaoPedido() == SituacaoPedido.FATURADO;

        // Publica evento para integração (após commit) se existir qualquer
        // possibilidade de criação/atualização
        if (faturadoAtual || faturadoAnterior) {
            eventPublisher.publishEvent(new PedidoAtualizadoEvent(
                    atualizado.getId(),
                    atualizado.getNumeroPedido(),
                    atualizado.getTipoPedido(),
                    atualizado.getValorTotal(),
                    faturadoAtual,
                    faturadoAnterior,
                    request.dataVencimentoTransacao(),
                    request.pagoTransacao(),
                    request.dataPagamentoTransacao()));
        }

        return pedidoMapper.toResponse(atualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .map(pedidoMapper::toResponse)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponse> listarTodos() {
        return pedidoMapper.toResponseList(pedidoRepository.findAll());
    }

    @Override
    @Transactional
    public void excluir(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new PedidoNaoEncontradoException(id);
        }
        pedidoRepository.deleteById(id);
    }
}
