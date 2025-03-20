package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ForumRequest")
public class ForumRequestDTO {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    private Long userId;

    @NotNull
    private Long bookId;

}
