package com.econome.pedidos;

import com.econome.pedidos.integration.transacao.config.TransacoesApiProperties;
import com.econome.pedidos.integration.participante.config.ParticipantesApiProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppEconomePedidosApplication {

	private static final Logger log = LoggerFactory.getLogger(AppEconomePedidosApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AppEconomePedidosApplication.class, args);
	}

	@Bean
	CommandLineRunner logIntegrations(TransacoesApiProperties transProps,
									  ParticipantesApiProperties partProps) {
		return args -> {
			log.info("[Startup] Transações baseUrl='{}' enabled={} ", transProps.baseUrl(), transProps.enabled());
			log.info("[Startup] Participantes baseUrl='{}' enabled={}", partProps.baseUrl(), partProps.enabled());
		};
	}
}
