package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.image.api.controller.uri;

import org.springframework.web.util.UriComponentsBuilder;

public class ImageUriFactory {

    private ImageUriFactory() {
    }

    public static String validUri(Long imageId) {
        return UriComponentsBuilder
                .fromPath("/api/v1/images")
                .pathSegment(String.valueOf(imageId))
                .build()
                .toUriString();
    }

}
