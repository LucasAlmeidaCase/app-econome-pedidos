package com.econome.pedidos.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.econome.repository")
@EntityScan("com.econome.domain")
public class JpaConfiguration {
}
