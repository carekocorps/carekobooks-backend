package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.constraints.Username;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUsernameChangeRequest implements BaseRequest {

    @Username
    private String username;

    @Username
    private String newUsername;

}
