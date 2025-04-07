package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookReviewResponse {

    private Long id;
    private String title;
    private String content;
    private Integer score;
    private UserResponse user;
    private BookResponse book;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
