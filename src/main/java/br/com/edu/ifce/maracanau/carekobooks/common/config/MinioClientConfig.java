package br.com.edu.ifce.maracanau.carekobooks.common.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfig {

    @Value("${minio.inner-endpoint}")
    private String innerEndpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(innerEndpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

}
