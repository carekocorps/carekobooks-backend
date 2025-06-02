package br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.response.BaseResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.simplified.SimplifiedUserResponse;
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
    private SimplifiedUserResponse user;
    private BookThreadResponse thread;

}
