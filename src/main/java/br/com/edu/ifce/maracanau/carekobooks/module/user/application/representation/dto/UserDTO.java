package br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.dto;

import br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.dto.ImageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "User")
public class UserDTO {

    private Long id;
    private String username;
    private String name;
    private String email;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ImageDTO image;

}
