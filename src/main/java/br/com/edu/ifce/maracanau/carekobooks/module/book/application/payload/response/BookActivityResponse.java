package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.response.BaseResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.simplified.SimplifiedBookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.simplified.SimplifiedUserResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookActivityResponse implements BaseResponse {

    private Long id;
    private BookProgressStatus status;
    private Integer pageCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SimplifiedUserResponse user;
    private SimplifiedBookResponse book;

}
