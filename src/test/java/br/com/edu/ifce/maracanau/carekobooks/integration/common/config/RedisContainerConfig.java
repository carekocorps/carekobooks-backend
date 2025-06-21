package br.com.edu.ifce.maracanau.carekobooks.integration.common.config;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;

@TestConfiguration(proxyBeanMethods = false)
public class RedisContainerConfig {

    private static final String IMAGE_NAME = "redis:8";

    @Bean
    @ServiceConnection
    public RedisContainer redis() {
        return new RedisContainer(IMAGE_NAME);
    }

}
