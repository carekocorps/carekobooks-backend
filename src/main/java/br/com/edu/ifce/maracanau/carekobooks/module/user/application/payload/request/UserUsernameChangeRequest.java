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
public class UserUsernameChangeRequest implements BaseRequest {

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^(?!_+$)[a-z0-9_]+$", message = "Username should only contain letters and numbers")
    @Schema(example = "string")
    private String username;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^(?!_+$)[a-z0-9_]+$", message = "Username should only contain letters and numbers")
    @Schema(example = "string")
    private String newUsername;

}
