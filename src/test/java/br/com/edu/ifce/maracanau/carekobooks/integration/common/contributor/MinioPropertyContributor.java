package br.com.edu.ifce.maracanau.carekobooks.integration.common.contributor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.MinIOContainer;

@Component
public class MinioPropertyContributor implements BasePropertyContributor {

    @Autowired(required = false)
    private MinIOContainer minio;

    @Override
    public void contribute(DynamicPropertyRegistry registry) {
        var uri = minio != null && minio.isRunning()
                ? minio.getS3URL()
                : "http://localhost:9000";

        registry.add("minio.inner-endpoint", () -> uri);
        registry.add("minio.outer-endpoint", () -> uri);
    }

}
