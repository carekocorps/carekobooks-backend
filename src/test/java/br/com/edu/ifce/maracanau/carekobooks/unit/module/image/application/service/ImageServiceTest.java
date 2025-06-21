package br.com.edu.ifce.maracanau.carekobooks.unit.module.image.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.MultipartFileFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.application.payload.response.ImageResponseFactory;
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
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
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
    void find_withExistingImage_shouldReturnImageResponse() {
        // Arrange
        var image = ImageFactory.validImage();
        var response = ImageResponseFactory.validResponse(image);

        when(imageRepository.findById(image.getId()))
                .thenReturn(Optional.of(image));

        when(imageMapper.toResponse(image))
                .thenReturn(response);

        // Act
        var result = imageService.find(image.getId());

        // Assert
        assertThat(result)
                .isPresent()
                .contains(response);

        verify(imageRepository, times(1)).findById(image.getId());
        verify(imageMapper, times(1)).toResponse(image);
    }

    @Test
    void find_withNonExistingImage_shouldReturnEmpty() {
        // Arrange
        var id = Math.abs(new Random().nextLong()) + 1;

        when(imageRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act
        var result = imageService.find(id);

        // Assert
        assertThat(result).isEmpty();
        verify(imageRepository, times(1)).findById(id);
        verify(imageMapper, never()).toResponse(any(Image.class));
    }

    @Test
    void create_withValidFile_shouldReturnImageResponse() {
        // Arrange
        var file = MultipartFileFactory.validFile();
        var image = ImageFactory.validImage(file);
        var response = ImageResponseFactory.validResponse(image);

        when(minioService.create(file))
                .thenReturn(file.getName());

        doNothing()
                .when(imageValidator)
                .validate(any(Image.class));

        when(imageRepository.save(any(Image.class)))
                .thenReturn(image);

        when(imageMapper.toResponse(image))
                .thenReturn(response);

        // Act
        var result = imageService.create(file);

        // Assert
        assertThat(result).isEqualTo(response);
        assertThat(image.getName()).isEqualTo(file.getName());
        verify(minioService, times(1)).create(file);
        verify(imageValidator, times(1)).validate(any(Image.class));
        verify(imageRepository, times(1)).save(any(Image.class));
        verify(imageMapper, times(1)).toResponse(image);
    }

    @Test
    void delete_withExistingImage_shouldSucceed() {
        // Arrange
        var image = ImageFactory.validImage();

        when(imageRepository.findById(image.getId()))
                .thenReturn(Optional.of(image));

        doNothing()
                .when(minioService)
                .delete(image.getName());

        doNothing()
                .when(imageRepository)
                .delete(image);

        // Act && Assert
        assertThatCode(() -> imageService.delete(image.getId())).doesNotThrowAnyException();
        verify(imageRepository, times(1)).findById(image.getId());
        verify(minioService, times(1)).delete(image.getName());
        verify(imageRepository, times(1)).delete(image);
    }

    @Test
    void delete_withNonExistingImage_shouldThrowNotFoundException() {
        // Arrange
        var id = Math.abs(new Random().nextLong()) + 1;

        when(imageRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act && Assert
        assertThatThrownBy(() -> imageService.delete(id)).isInstanceOf(ImageNotFoundException.class);
        verify(imageRepository, times(1)).findById(id);
        verify(minioService, never()).delete(any(String.class));
        verify(imageRepository, never()).deleteById(any(Long.class));
    }

}
