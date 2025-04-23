package br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.response.BaseResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.response.ImageResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse implements BaseResponse {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ImageResponse image;

}
