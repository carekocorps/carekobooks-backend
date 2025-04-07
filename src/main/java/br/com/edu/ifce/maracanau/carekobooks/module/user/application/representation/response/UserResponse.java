package br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.response.ImageResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ImageResponse image;

}
