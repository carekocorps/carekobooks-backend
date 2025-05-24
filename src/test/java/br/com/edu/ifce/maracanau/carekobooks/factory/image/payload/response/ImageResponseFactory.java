package br.com.edu.ifce.maracanau.carekobooks.factory.image.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.factory.image.infrastructure.domain.entity.ImageFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import org.springframework.web.util.UriComponentsBuilder;

public class ImageResponseFactory {

    private ImageResponseFactory() {
    }

    public static ImageResponse validResponse(String outerEndpoint, String bucket) {
        var image = ImageFactory.validImage();
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

    public static String validResponseUrl(String name, String outerEndpoint, String bucket) {
        return UriComponentsBuilder
                .fromUriString(outerEndpoint)
                .pathSegment(bucket)
                .pathSegment(name)
                .toUriString();
    }

}
