package com.econome.repository;

import com.econome.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório Spring Data JPA para a entidade Pedido.
 * Mantém o acesso a dados desacoplado da camada de serviço e evita código boilerplate de persistência.
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
