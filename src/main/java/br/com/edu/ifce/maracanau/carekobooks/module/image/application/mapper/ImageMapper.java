package br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.model.Image;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    Image toModel(ImageResponse response);
    ImageResponse toResponse(Image image);

}
