package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "BookActivity")
public class BookActivityDTO {

    private Long id;
    private BookProgressStatus status;
    private Integer pagesRead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDTO user;
    private BookDTO book;

}
