package br.com.edu.ifce.maracanau.carekobooks.factory.image.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.factory.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import com.github.javafaker.Faker;
import org.springframework.web.util.UriComponentsBuilder;

public class ImageResponseFactory {

    private static final Faker faker = new Faker();

    private ImageResponseFactory() {
    }

    public static ImageResponse validResponse(Image image, String outerEndpoint, String bucket) {
        var response = new ImageResponse();
        response.setId(image.getId());
        response.setName(image.getName());
        response.setUrl(validResponseUrl(image.getName(), outerEndpoint, bucket));
        response.setContentType(image.getContentType());
        response.setSizeInBytes(image.getSizeInBytes());
        response.setCreatedAt(image.getCreatedAt());
        response.setUpdatedAt(image.getUpdatedAt());
        return response;
    }

    public static ImageResponse validResponse(Image image) {
        return validResponse(image, faker.internet().url(), faker.app().name());
    }

    public static ImageResponse validResponse() {
        return validResponse(ImageFactory.validImage());
    }

    public static ImageResponse validResponse(String outerEndpoint, String bucket) {
        var image = ImageFactory.validImage();
        return validResponse(image, outerEndpoint, bucket);
    }

    public static String validResponseUrl(String name, String outerEndpoint, String bucket) {
        return UriComponentsBuilder
                .fromUriString(outerEndpoint)
                .pathSegment(bucket)
                .pathSegment(name)
                .toUriString();
    }

}
