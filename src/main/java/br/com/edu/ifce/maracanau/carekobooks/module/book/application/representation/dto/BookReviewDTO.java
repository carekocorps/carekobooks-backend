package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.dto.UserDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(name = "BookReview")
public class BookReviewDTO {

    private Long id;
    private String title;
    private String content;
    private Integer score;
    private UserDTO user;
    private BookDTO book;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
