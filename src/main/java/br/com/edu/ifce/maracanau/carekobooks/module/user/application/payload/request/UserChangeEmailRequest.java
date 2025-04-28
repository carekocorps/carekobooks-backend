package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserChangeEmailRequest implements BaseRequest {

    @NotBlank
    @Email
    @Size(max = 255)
    private String currentEmail;

    @NotBlank
    @Email
    @Size(max = 255)
    private String newEmail;

    @NotBlank
    @Size(max = 8)
    private String otp;

}
