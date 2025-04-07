package br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ImageResponse {

    private Long id;
    private String name;
    private String url;
    private String contentType;
    private Long sizeInBytes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
