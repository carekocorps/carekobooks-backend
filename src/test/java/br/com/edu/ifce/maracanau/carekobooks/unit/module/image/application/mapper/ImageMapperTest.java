package br.com.edu.ifce.maracanau.carekobooks.unit.module.image.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.image.application.payload.response.ImageResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void toEntity_withNullResponse_shouldReturnNull() {
        // Act
        var result = imageMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_withValidResponse_shouldReturnValidEntity() {
        // Arrange
        var response = ImageResponseFactory.validResponse(OUTER_ENDPOINT, BUCKET);

        // Act
        var result = imageMapper.toEntity(response);

        // Assert
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
        assertEquals(response.getContentType(), result.getContentType());
        assertEquals(response.getSizeInBytes(), result.getSizeInBytes());
        assertEquals(response.getCreatedAt(), result.getCreatedAt());
        assertEquals(response.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void toResponse_withNullEntity_shouldReturnNull() {
        // Arrange
        Image image = null;

        // Act
        var result = imageMapper.toResponse(image);

        // Assert
        assertNull(result);
    }

    @Test
    void toResponse_withValidEntity_shouldReturnValidResponse() {
        // Arrange
        var image = ImageFactory.validImage();
        var expectedUrl = ImageResponseFactory.validResponseUrl(image.getName(), OUTER_ENDPOINT, BUCKET);

        // Act
        var result = imageMapper.toResponse(image);

        // Assert
        assertEquals(result.getId(), image.getId());
        assertEquals(result.getName(), image.getName());
        assertEquals(result.getUrl(), expectedUrl);
        assertEquals(result.getContentType(), image.getContentType());
        assertEquals(result.getSizeInBytes(), image.getSizeInBytes());
        assertEquals(result.getCreatedAt(), image.getCreatedAt());
        assertEquals(result.getUpdatedAt(), image.getUpdatedAt());
    }

}
