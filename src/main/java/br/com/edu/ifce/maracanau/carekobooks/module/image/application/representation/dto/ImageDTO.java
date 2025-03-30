package br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "Image")
public class ImageDTO {

    private Long id;
    private String name;
    private String url;
    private String contentType;
    private Long size;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
