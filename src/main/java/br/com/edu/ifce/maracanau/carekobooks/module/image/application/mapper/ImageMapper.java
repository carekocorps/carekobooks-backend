package br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.dto.ImageDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.model.Image;
import br.com.edu.ifce.maracanau.carekobooks.shared.util.MinioUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

@Mapper(componentModel = "spring")
public abstract class ImageMapper {

    @Autowired
    protected MinioUtils minioUtils;

    @Mapping(target = "name", expression = "java(minioUtils.upload(file))")
    @Mapping(target = "url", expression = "java(minioUtils.getUrl(image.getName()))")
    @Mapping(target = "size", expression = "java((Long) file.getSize())")
    public abstract Image toModel(MultipartFile file) throws Exception;
    public abstract ImageDTO toDTO(Image file);

}
