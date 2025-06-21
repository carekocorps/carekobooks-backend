package br.com.edu.ifce.maracanau.carekobooks.integration.common.contributor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.MinIOContainer;

import java.util.UUID;

@Component
public class MinioPropertyContributor implements BasePropertyContributor {

    @Autowired(required = false)
    private MinIOContainer minioContainer;

    @Override
    public void contribute(DynamicPropertyRegistry registry) {
        if (minioContainer != null && minioContainer.isRunning()) {
            registry.add("minio.inner-endpoint", minioContainer::getS3URL);
            registry.add("minio.outer-endpoint", minioContainer::getS3URL);
            registry.add("minio.access-key", minioContainer::getUserName);
            registry.add("minio.secret-key", minioContainer::getPassword);
        } else {
            registry.add("minio.inner-endpoint", () -> "http://localhost:9000");
            registry.add("minio.outer-endpoint", () -> "http://localhost:9000");
            registry.add("minio.access-key", () -> UUID.randomUUID().toString());
            registry.add("minio.secret-key", () -> UUID.randomUUID().toString());
        }
    }

}
