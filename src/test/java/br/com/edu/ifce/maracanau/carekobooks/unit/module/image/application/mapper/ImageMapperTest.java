package br.com.edu.ifce.maracanau.carekobooks.unit.module.image.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.UnitTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.application.payload.response.ImageResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@UnitTest
class ImageMapperTest {

    private static final String OUTER_ENDPOINT = "https://outerendpoint.com";
    private static final String BUCKET = "bucket";

    private ImageMapper imageMapper;

    @BeforeEach
    void setUp() {
        imageMapper = Mappers.getMapper(ImageMapper.class);
        setField(imageMapper, "outerEndpoint", OUTER_ENDPOINT);
        setField(imageMapper, "bucket", BUCKET);
    }

    @Test
    void toEntity_withNullImageResponse_shouldReturnNullImage() {
        // Arrange
        ImageResponse response = null;

        // Act
        var result = imageMapper.toEntity(response);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void toEntity_withValidImageResponse_shouldReturnImage() {
        // Arrange
        var response = ImageResponseFactory.validResponse(OUTER_ENDPOINT, BUCKET);

        // Act
        var result = imageMapper.toEntity(response);

        // Assert
        assertThat(result.getId()).isEqualTo(response.getId());
        assertThat(result.getName()).isEqualTo(response.getName());
        assertThat(result.getContentType()).isEqualTo(response.getContentType());
        assertThat(result.getSizeInBytes()).isEqualTo(response.getSizeInBytes());
        assertThat(result.getCreatedAt()).isEqualToIgnoringNanos(response.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualToIgnoringNanos(response.getUpdatedAt());
    }

    @Test
    void toResponse_withNullEntity_shouldReturnNull() {
        // Arrange
        Image image = null;

        // Act
        var result = imageMapper.toResponse(image);

        // Assert
        assertThat(result).isNull();
    }

    @Test
    void toResponse_withValidImage_shouldReturnImageResponse() {
        // Arrange
        var image = ImageFactory.validImage();
        var expectedUrl = ImageResponseFactory.validResponseUrl(image.getName(), OUTER_ENDPOINT, BUCKET);

        // Act
        var result = imageMapper.toResponse(image);

        // Assert
        assertThat(result.getId()).isEqualTo(image.getId());
        assertThat(result.getName()).isEqualTo(image.getName());
        assertThat(result.getUrl()).isEqualTo(expectedUrl);
        assertThat(result.getContentType()).isEqualTo(image.getContentType());
        assertThat(result.getSizeInBytes()).isEqualTo(image.getSizeInBytes());
        assertThat(result.getCreatedAt()).isEqualToIgnoringNanos(image.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualToIgnoringNanos(image.getUpdatedAt());
    }

}
