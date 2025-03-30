package br.com.edu.ifce.maracanau.carekobooks.shared.util;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class MinioUtils {

    @Value("${minio.bucket}")
    private String minioBucket;

    private final MinioClient minioClient;

    public String getUrl(String fileName) throws Exception {
        return minioClient
                .getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(minioBucket)
                .object(fileName)
                .method(Method.GET)
                .expiry(7, TimeUnit.DAYS)
                .build()
        );
    }

    public String upload(MultipartFile file) throws Exception {
        var fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioBucket)
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build()
        );

        return fileName;
    }

    public void delete(String fileName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioBucket).object(fileName).build());
    }

}
