package com.econome.pedidos.service;

import com.econome.pedidos.dto.PedidoRequest;
import com.econome.pedidos.dto.PedidoResponse;

import java.util.List;

public interface PedidoService {

    PedidoResponse criar(PedidoRequest request);

    PedidoResponse atualizar(Long id, PedidoRequest request);

    PedidoResponse buscarPorId(Long id);

    List<PedidoResponse> listarTodos();

    void excluir(Long id);
}

