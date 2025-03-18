package br.com.edu.ifce.maracanau.carekobooks.dto.thread;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "Thread")
public class ThreadDTO {

    private Long id;
    private String title;
    private String description;

}
