package br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequest implements BaseRequest {

    @NotBlank
    private String refreshToken;

}
