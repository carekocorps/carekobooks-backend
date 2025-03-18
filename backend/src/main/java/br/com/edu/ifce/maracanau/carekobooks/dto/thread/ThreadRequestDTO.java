package br.com.edu.ifce.maracanau.carekobooks.dto.thread;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "ThreadRequest")
public class ThreadRequestDTO {

    private String title;
    private String description;

}
