package br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest implements BaseRequest {

    @NotBlank
    @Size(max = 255)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String password;

}
