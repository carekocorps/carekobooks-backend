package br.com.edu.ifce.maracanau.carekobooks.integration.module.image.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.IntegrationTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.api.controller.uri.ImageUriFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.DynamicPropertyRegistrarConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.KeycloakContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.repository.ImageRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Import({
        DynamicPropertyRegistrarConfig.class,
        KeycloakContainerConfig.class,
        PostgresContainerConfig.class
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImageControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        imageRepository.deleteAll();
    }

    @Test
    void find_withNonExistingImage_shouldReturnNoContent() {
        // Arrange
        var imageId = Math.abs(new Random().nextLong()) + 1;

        // Act
        var uri = ImageUriFactory.validUri(imageId);
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, ImageResponse.class);

        // Assert
        assertThat(imageRepository.count()).isZero();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void find_withExistingImage_shouldReturnImageResponse() {
        // Arrange
        var image = imageRepository.save(ImageFactory.validImageWithNullId());

        // Act
        var uri = ImageUriFactory.validUri(image.getId());
        var response = restTemplate.exchange(uri, HttpMethod.GET, null, ImageResponse.class);
        var result = response.getBody();

        // Assert
        assertThat(imageRepository.count()).isEqualTo(1);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(image.getId());
    }

}
