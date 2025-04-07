package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookActivityResponse {

    private Long id;
    private BookProgressStatus status;
    private Integer pagesRead;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponse user;
    private BookResponse book;

}
