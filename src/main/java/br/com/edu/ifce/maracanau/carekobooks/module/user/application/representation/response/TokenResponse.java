package br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TokenResponse {

    private String username;
    private Boolean isAuthenticated;
    private Date createdAt;
    private Date expiresAt;
    private String accessToken;
    private String refreshToken;

}
