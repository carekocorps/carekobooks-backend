package br.com.edu.ifce.maracanau.carekobooks.integration.common.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MinIOContainer;

@Import({DynamicPropertyRegistrarConfig.class})
@TestConfiguration(proxyBeanMethods = false)
public class MinioContainerConfig {

    private static final String DOCKER_IMAGE = "minio/minio:RELEASE.2025-05-24T17-08-30Z";

    @Bean
    public MinIOContainer minioContainer() {
        return new MinIOContainer(DOCKER_IMAGE);
    }

}
