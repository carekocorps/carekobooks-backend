package br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request;

import br.com.edu.ifce.maracanau.carekobooks.shared.application.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserRegisterRequest implements BaseRequest {

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-z_0-9]+$", message = "Username should only contain letters and numbers")
    @Schema(example = "string")
    private String username;

    @Size(max = 50)
    @Schema(example = "string")
    private String name;

    @NotBlank
    @Email
    @Size(max = 255)
    @Schema(example = "string@gmail.com")
    private String email;

    @NotBlank
    @Size(min = 5, max = 255)
    @Pattern(regexp = "^\\S+$", message = "Password should not contain spaces")
    @Schema(example = "string")
    private String password;

    @Size(max = 1000)
    @Schema(example = "string")
    private String description;

}
