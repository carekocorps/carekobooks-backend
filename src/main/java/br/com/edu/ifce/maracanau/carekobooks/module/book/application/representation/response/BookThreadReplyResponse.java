package br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.response.BaseResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookThreadReplyResponse implements BaseResponse {

    private Long id;
    private String content;
    private Boolean isContainingChildren;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserResponse user;
    private BookThreadResponse thread;

}
