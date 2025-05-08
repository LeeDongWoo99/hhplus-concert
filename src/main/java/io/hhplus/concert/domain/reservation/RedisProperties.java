package io.hhplus.concert.domain.reservation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.redis")
@Component
@Getter
@Setter
public class RedisProperties {
    private String host;
    private int port;
}

