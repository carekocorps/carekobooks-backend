package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordRecoveryRequest implements BaseRequest {

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-z0-9]+$", message = "Username should only contain letters and numbers")
    @Schema(example = "string")
    private String username;

    @NotBlank
    @Size(min = 5, max = 255)
    @Pattern(regexp = "^\\S+$", message = "Password should not contain spaces")
    @Schema(example = "string")
    private String newPassword;

}
