package br.com.edu.ifce.maracanau.carekobooks.integration.common.provider;

import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MinioProvider {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public void setUp() throws Exception {
        if (!bucketExists()) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    public void tearDown() throws Exception {
        if (bucketExists()) {
            for (var item : minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).recursive(true).build())) {
                minioClient.removeObject(RemoveObjectArgs.builder()
                        .bucket(bucket)
                        .object(item.get().objectName())
                        .build());
            }
        }
    }

    private boolean bucketExists() throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    }

}
