package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.annotation.Password;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.annotation.Username;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest implements BaseRequest {

    @Username
    private String username;

    @Size(max = 50)
    private String displayName;

    @NotBlank
    @Email
    @Size(max = 255)
    @Schema(example = "string@gmail.com")
    private String email;

    @Password
    private String password;

    @Size(max = 1000)
    private String description;

}
