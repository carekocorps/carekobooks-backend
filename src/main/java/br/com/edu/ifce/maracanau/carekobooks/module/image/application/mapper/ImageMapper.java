package br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.MinioService;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public abstract class ImageMapper {

    @Autowired
    protected MinioService minioService;

    @Mapping(target = "name", expression = "java(minioService.upload(file))")
    @Mapping(target = "url", expression = "java(minioService.findUrlByFilename(image.getName()))")
    @Mapping(target = "sizeInBytes", source = "size")
    public abstract Image toModel(MultipartFile file) throws Exception;
    public abstract Image toModel(ImageResponse imageResponse);
    public abstract ImageResponse toResponse(Image file);

}
