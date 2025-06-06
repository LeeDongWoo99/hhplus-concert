package io.hhplus.concert.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.data.redis")
@Component
@Getter
@Setter
public class RedisProperties {
    private String host;
    private int port;
}

