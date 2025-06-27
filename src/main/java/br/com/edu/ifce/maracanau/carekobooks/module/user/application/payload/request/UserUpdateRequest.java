package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest implements BaseRequest {

    @Size(max = 50)
    private String displayName;

    @Size(max = 1000)
    private String description;

    @NotNull
    private Boolean retainCurrentImage;

}
