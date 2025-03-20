package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Forum")
public class ForumDTO {

    private Long id;
    private String title;
    private String description;

}
