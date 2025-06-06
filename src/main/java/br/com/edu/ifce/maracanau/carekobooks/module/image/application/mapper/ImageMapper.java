package br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;

@Mapper(
        componentModel = "spring",
        imports = UriComponentsBuilder.class
)
public abstract class ImageMapper {

    @Value("${minio.outer-endpoint}")
    protected String outerEndpoint;

    @Value("${minio.bucket}")
    protected String bucket;

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "books", ignore = true)
    public abstract Image toEntity(ImageResponse response);

    @Mapping(
            target = "url",
            expression = """
                java(
                    UriComponentsBuilder
                        .fromUriString(outerEndpoint)
                        .pathSegment(bucket)
                        .pathSegment(image.getName())
                        .toUriString()
                )
                """
    )
    public abstract ImageResponse toResponse(Image image);

}
