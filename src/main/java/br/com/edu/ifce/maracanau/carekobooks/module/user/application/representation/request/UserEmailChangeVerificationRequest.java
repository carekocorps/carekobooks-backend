package br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEmailChangeVerificationRequest {

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
    private String emailResetToken;

}
