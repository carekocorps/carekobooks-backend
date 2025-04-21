package br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.request.BaseRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserPasswordResetRequest implements BaseRequest {

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(max = 255)
    private String password;

    @NotNull
    private UUID resetToken;

}
