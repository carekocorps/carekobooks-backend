package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookGenreResponse implements BaseResponse {

    private Long id;
    private String name;
    private String displayName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
