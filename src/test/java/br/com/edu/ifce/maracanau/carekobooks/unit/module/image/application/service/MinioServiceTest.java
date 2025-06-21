package br.com.edu.ifce.maracanau.carekobooks.unit.module.image.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.MultipartFileFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.MinioService;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageDeletionException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageUploadException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
@ExtendWith(MockitoExtension.class)
class MinioServiceTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinioService minioService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(minioService, "minioBucket", "bucket");
    }

    @Test
    void create_withPutObjectSucceeding_shouldReturnValidFilename() throws Exception {
        // Arrange
        var file = MultipartFileFactory.validFile();

        // Act
        var filename = minioService.create(file);

        // Assert
        assertNotNull(filename);
        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
    }

    @Test
    void create_withPutObjectFailing_shouldFail() throws Exception {
        // Arrange
        var file = MultipartFileFactory.validFile();

        doThrow(new RuntimeException())
                .when(minioClient)
                .putObject(any(PutObjectArgs.class));

        // Act & Assert
        assertThrows(ImageUploadException.class, () -> minioService.create(file));
        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
    }

    @Test
    void delete_withRemoveObjectSucceeding_shouldPass() throws Exception {
        // Arrange
        var filename = ImageFactory.validImage().getName();

        // Act && Assert
        assertDoesNotThrow(() -> minioService.delete(filename));
        verify(minioClient, times(1)).removeObject(any(RemoveObjectArgs.class));
    }

    @Test
    void delete_withRemoveObjectFailing_shouldFail() throws Exception {
        // Arrange
        var filename = ImageFactory.validImage().getName();

        doThrow(new RuntimeException())
                .when(minioClient)
                .removeObject(any(RemoveObjectArgs.class));

        // Act && Assert
        assertThrows(ImageDeletionException.class, () -> minioService.delete(filename));
        verify(minioClient, times(1)).removeObject(any(RemoveObjectArgs.class));
    }

}
