package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.response.BaseResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookReviewResponse implements BaseResponse {

    private Long id;
    private String title;
    private String content;
    private Integer score;
    private UserResponse user;
    private BookResponse book;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
