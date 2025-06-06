package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.simplified;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.response.BaseResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SimplifiedUserResponse implements BaseResponse {

    private Long id;
    private UUID keycloakId;
    private String username;
    private String displayName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ImageResponse image;

}
