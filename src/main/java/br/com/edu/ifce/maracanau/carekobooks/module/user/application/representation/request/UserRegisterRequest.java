package br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request;

import br.com.edu.ifce.maracanau.carekobooks.shared.layer.application.request.BaseRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest implements BaseRequest {

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[a-z_0-9]+$", message = "Username should only contain letters and numbers")
    private String username;

    @Size(max = 50)
    private String name;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @Size(min = 5, max = 255)
    @Pattern(regexp = "^\\S+$", message = "Password should not contain spaces")
    private String password;

    @Size(max = 1000)
    private String description;

}
