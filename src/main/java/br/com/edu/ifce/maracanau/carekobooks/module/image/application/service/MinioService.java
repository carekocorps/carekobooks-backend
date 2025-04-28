package br.com.edu.ifce.maracanau.carekobooks.module.image.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.exception.ImageDeletionException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.exception.ImageRetrievalException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.exception.ImageUploadException;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class MinioService {

    @Value("${minio.bucket}")
    private String minioBucket;

    private final MinioClient minioClient;

    public String findUrlByFilename(String filename) {
        try {
            return minioClient
                    .getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                            .bucket(minioBucket)
                            .object(filename)
                            .method(Method.GET)
                            .expiry(7, TimeUnit.DAYS)
                            .build()
                    );
        } catch (Exception e) {
            throw new ImageRetrievalException(e.getMessage());
        }
    }

    public String upload(MultipartFile file) {
        try {
            var fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioBucket)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );

            return fileName;
        } catch (Exception e) {
            throw new ImageUploadException(e.getMessage());
        }
    }

    public void deleteByFilename(String filename) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioBucket).object(filename).build());
        } catch (Exception e) {
            throw new ImageDeletionException(e.getMessage());
        }
    }

}
