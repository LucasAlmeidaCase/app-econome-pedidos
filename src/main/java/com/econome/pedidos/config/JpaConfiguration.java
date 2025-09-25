package com.econome.pedidos.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Configuração JPA centralizada para o contexto do serviço de pedidos.
 * Habilita o scan de repositórios e entidades, mantendo a organização por camadas/pacotes
 * e evitando mistura de responsabilidades.
 */
@Configuration
@EnableJpaRepositories("com.econome.repository")
@EntityScan("com.econome.domain")
public class JpaConfiguration {
}
