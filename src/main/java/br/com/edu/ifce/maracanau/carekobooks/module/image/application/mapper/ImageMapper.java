package br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "users", ignore = true)
    @Mapping(target = "books", ignore = true)
    Image toModel(ImageResponse response);
    ImageResponse toResponse(Image image);

}
