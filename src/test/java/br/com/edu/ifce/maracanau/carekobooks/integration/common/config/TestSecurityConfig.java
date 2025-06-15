package br.com.edu.ifce.maracanau.carekobooks.integration.common.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return mock(JwtDecoder.class);
    }

}
