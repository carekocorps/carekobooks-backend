package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.response.BaseResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookProgressResponse implements BaseResponse {

    private Long id;
    private BookProgressStatus status;
    private Boolean isFavorite;
    private Integer score;
    private Integer pageCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponse user;
    private BookResponse book;

}
