package br.com.edu.ifce.maracanau.carekobooks.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.username}")
    private String username;

    @Value("${minio.password}")
    private String password;

    @Bean
    MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(username, password)
                .build();
    }

}
