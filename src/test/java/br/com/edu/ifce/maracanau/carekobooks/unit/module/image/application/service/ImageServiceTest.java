package br.com.edu.ifce.maracanau.carekobooks.unit.module.image.application.service;

import br.com.edu.ifce.maracanau.carekobooks.factory.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.image.infrastructure.domain.entity.MultipartFileFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.image.payload.response.ImageResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.MinioService;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.validator.ImageValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private MinioService minioService;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageMapper imageMapper;

    @Mock
    private ImageValidator imageValidator;

    @InjectMocks
    private ImageService imageService;

    @Test
    void find_withExistingImage_shouldReturnValidResponse() {
        // Arrange
        var image = ImageFactory.validImage();
        var response = ImageResponseFactory.validResponse(image, "https://outerendpoint.com", "bucket");

        when(imageRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(image));

        when(imageMapper.toResponse(any(Image.class)))
                .thenReturn(response);

        // Act
        var result = imageService.find(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(result.get(), response);
        verify(imageRepository).findById(any(Long.class));
    }

    @Test
    void find_withNonExistingImage_shouldReturnEmpty() {
        // Arrange
        when(imageRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        // Act
        var result = imageService.find(1L);

        // Assert
        assertTrue(result.isEmpty());
        verify(imageRepository).findById(any(Long.class));
    }

    @Test
    void create_withValidFile_shouldReturnValidResponse() {
        // Arrange
        var file = MultipartFileFactory.validFile();
        var image = ImageFactory.validImage(file);
        var response = ImageResponseFactory.validResponse(image, "https://outerendpoint.com", "bucket");

        when(imageRepository.save(any(Image.class)))
                .thenReturn(image);

        when(imageMapper.toResponse(image))
                .thenReturn(response);

        // Act
        var result = imageService.create(file);

        // Assert
        assertEquals(response, result);
        verify(imageValidator).validate(any(Image.class));
        verify(minioService).create(file);
        verify(imageRepository).save(any(Image.class));
    }

    @Test
    void delete_withExistingImage_shouldPass() {
        // Arrange
        var image = ImageFactory.validImage();

        when(imageRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(image));

        // Act && Assert
        assertDoesNotThrow(() -> imageService.delete(1L));
        verify(minioService).delete(any(String.class));
        verify(imageRepository).deleteById(any(Long.class));
    }

    @Test
    void delete_withNonExistingImage_shouldFail() {
        // Arrange
        when(imageRepository.findById(any(Long.class)))
                .thenReturn(Optional.empty());

        // Act && Assert
        assertThrows(ImageNotFoundException.class, () -> imageService.delete(1L));
    }

}
