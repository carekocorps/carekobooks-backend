package br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.response.BaseResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserResponse implements BaseResponse {

    private Long id;
    private UUID keycloakId;
    private String username;
    private String displayName;
    private String description;
    private Integer progressesCount;
    private Integer activitiesCount;
    private Integer reviewsCount;
    private Integer threadsCount;
    private Integer repliesCount;
    private Integer followingCount;
    private Integer followersCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ImageResponse image;

}
