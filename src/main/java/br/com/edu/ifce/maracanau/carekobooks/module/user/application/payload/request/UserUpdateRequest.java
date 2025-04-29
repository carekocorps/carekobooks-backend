package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.constraints.Password;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.constraints.Username;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest implements BaseRequest {

    @Username
    private String username;

    @Size(max = 50)
    private String displayName;

    @Password
    private String password;

    @Size(max = 1000)
    @Schema(example = "string")
    private String description;

}
