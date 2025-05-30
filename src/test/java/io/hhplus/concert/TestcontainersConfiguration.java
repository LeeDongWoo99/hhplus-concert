package io.hhplus.concert;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;

@Configuration
public class TestcontainersConfiguration {

	public static final MySQLContainer<?> MYSQL_CONTAINER;
	public static final GenericContainer<?> REDIS_CONTAINER;
	public static final KafkaContainer KAFKA_CONTAINER;

	static {
		// --- MySQL ---
		MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
				.withDatabaseName("hhplus")
				.withUsername("test")
				.withPassword("test");
		MYSQL_CONTAINER.start();

		// --- Redis ---
		REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:7.2.3"))
				.withExposedPorts(6379)
				.withStartupTimeout(Duration.ofSeconds(30));
		REDIS_CONTAINER.start();

		// --- Kafka ---
		KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("apache/kafka-native:3.8.0"));
		KAFKA_CONTAINER.setPortBindings(Collections.singletonList("9092:9092"));
		KAFKA_CONTAINER.start();

		// System property로 Spring에 주입
		System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC");
		System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
		System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());

		System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost());
		System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());

		System.setProperty("spring.kafka.bootstrap-servers", KAFKA_CONTAINER.getBootstrapServers());
	}

	@PreDestroy
	public void preDestroy() {
		if (MYSQL_CONTAINER.isRunning()) MYSQL_CONTAINER.stop();
		if (REDIS_CONTAINER.isRunning()) REDIS_CONTAINER.stop();
		if (KAFKA_CONTAINER.isRunning()) KAFKA_CONTAINER.stop();
	}
}


