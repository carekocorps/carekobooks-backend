package br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.response;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ImageResponse implements BaseResponse {

    private Long id;
    private String name;
    private String url;
    private String contentType;
    private Long sizeInBytes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
