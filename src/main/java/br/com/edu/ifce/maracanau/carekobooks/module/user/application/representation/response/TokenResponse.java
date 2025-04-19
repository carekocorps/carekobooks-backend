package br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TokenResponse implements BaseResponse {

    private String username;
    private Boolean isAuthenticated;
    private LocalDateTime createdAt;
    private LocalDateTime accessExpiresAt;
    private LocalDateTime refreshExpiresAt;
    private String accessToken;
    private String refreshToken;

}
