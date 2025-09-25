package com.econome.pedidos.dto;

import com.econome.domain.Pedido;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    Pedido toEntity(PedidoRequest request);

    void updateEntityFromRequest(PedidoRequest request, @MappingTarget Pedido entity);

    PedidoResponse toResponse(Pedido entity);

    List<PedidoResponse> toResponseList(List<Pedido> entities);
}