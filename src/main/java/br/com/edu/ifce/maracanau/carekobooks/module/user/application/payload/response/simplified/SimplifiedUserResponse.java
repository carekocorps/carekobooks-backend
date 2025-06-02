package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.simplified;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.response.BaseResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SimplifiedUserResponse implements BaseResponse {

    private Long id;
    private String username;
    private String displayName;
    private UserRole role;
    private Boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ImageResponse image;

}
