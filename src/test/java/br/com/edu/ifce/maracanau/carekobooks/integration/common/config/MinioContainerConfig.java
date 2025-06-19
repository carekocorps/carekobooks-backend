package br.com.edu.ifce.maracanau.carekobooks.integration.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MinIOContainer;

@TestConfiguration(proxyBeanMethods = false)
public class MinioContainerConfig {

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Bean
    public MinIOContainer minio() {
        return new MinIOContainer("minio/minio:RELEASE.2025-05-24T17-08-30Z")
                .withUserName(accessKey)
                .withPassword(secretKey);
    }

}
