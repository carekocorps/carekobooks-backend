package br.com.edu.ifce.maracanau.carekobooks.unit.module.image.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.factory.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.image.payload.response.ImageResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

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
    void toModel_withValidResponse_shouldReturnValidModel() {
        // Arrange
        var response = ImageResponseFactory.validResponse(OUTER_ENDPOINT, BUCKET);

        // Act
        var result = imageMapper.toModel(response);

        // Assert
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
        assertEquals(response.getContentType(), result.getContentType());
        assertEquals(response.getSizeInBytes(), result.getSizeInBytes());
        assertEquals(response.getCreatedAt(), result.getCreatedAt());
        assertEquals(response.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void toResponse_withValidModel_shouldReturnValidResponse() {
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
