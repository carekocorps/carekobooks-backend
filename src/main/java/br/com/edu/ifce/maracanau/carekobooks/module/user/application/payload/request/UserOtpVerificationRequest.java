package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.request.BaseRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.constraints.Username;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.enums.OtpValidationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOtpVerificationRequest implements BaseRequest {

    @Username
    private String username;

    @NotBlank
    @Size(min = 8, max = 8)
    private String otp;

    @NotNull
    private OtpValidationType validationType;

}
