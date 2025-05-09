package br.com.edu.ifce.maracanau.carekobooks.module.image.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageDeletionException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageRetrievalException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageUploadException;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class MinioService {

    @Value("${minio.bucket}")
    private String minioBucket;

    private final MinioClient minioClient;

    public String create(MultipartFile file) {
        try {
            var filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioBucket)
                    .object(filename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );

            return filename;
        } catch (Exception e) {
            throw new ImageUploadException(e.getMessage());
        }
    }

    public void delete(String filename) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioBucket).object(filename).build());
        } catch (Exception e) {
            throw new ImageDeletionException(e.getMessage());
        }
    }

}
