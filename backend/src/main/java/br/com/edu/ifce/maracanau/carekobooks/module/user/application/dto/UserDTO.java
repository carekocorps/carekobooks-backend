package br.com.edu.ifce.maracanau.carekobooks.module.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "User")
public class UserDTO {

    private String username;
    private String email;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
