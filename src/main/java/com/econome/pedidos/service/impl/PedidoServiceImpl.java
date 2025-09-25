package com.econome.pedidos.service.impl;

import com.econome.domain.Pedido;
import com.econome.pedidos.dto.PedidoMapper;
import com.econome.pedidos.dto.PedidoRequest;
import com.econome.pedidos.dto.PedidoResponse;
import com.econome.pedidos.exception.PedidoNaoEncontradoException;
import com.econome.pedidos.service.PedidoService;
import com.econome.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;

    @Override
    @Transactional
    public PedidoResponse criar(PedidoRequest request) {
        Pedido entity = pedidoMapper.toEntity(request);
        Pedido salvo = pedidoRepository.save(entity);
        return pedidoMapper.toResponse(salvo);
    }

    @Override
    @Transactional
    public PedidoResponse atualizar(Long id, PedidoRequest request) {
        Pedido existente = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNaoEncontradoException(id));
        pedidoMapper.updateEntityFromRequest(request, existente);
        Pedido atualizado = pedidoRepository.save(existente);
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
