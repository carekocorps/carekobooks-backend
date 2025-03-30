package br.com.edu.ifce.maracanau.carekobooks.module.file.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "StoredFile")
public class StoredFileDTO {

    private Long id;
    private String fileName;
    private String url;

}
