package br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller.docs.UserSocialControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.query.UserSocialQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.enums.UserRelationship;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.UserSocialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users/{username}/social")
@Tag(name = "User Social", description = "Endpoints for managing user social interactions")
public class UserSocialController implements BaseController, UserSocialControllerDocs {

    private final UserSocialService userSocialService;

    @Override
    @GetMapping("/followers")
    public ResponseEntity<ApplicationPage<UserResponse>> searchFollowers(@PathVariable String username, @ParameterObject UserSocialQuery query) {
        query.setUsername(username);
        query.setRelationship(UserRelationship.FOLLOWER);
        var responses = userSocialService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/following")
    public ResponseEntity<ApplicationPage<UserResponse>> searchFollowing(@PathVariable String username, @ParameterObject UserSocialQuery query) {
        query.setUsername(username);
        query.setRelationship(UserRelationship.FOLLOWING);
        var responses = userSocialService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @UserRoleRequired
    @PostMapping("/following/{targetUsername}")
    public ResponseEntity<Void> follow(@PathVariable String username, @PathVariable String targetUsername) {
        userSocialService.changeFollowing(username, targetUsername, true);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/following/{targetUsername}")
    public ResponseEntity<Void> unfollow(@PathVariable String username, @PathVariable String targetUsername) {
        userSocialService.changeFollowing(username, targetUsername, false);
        return ResponseEntity.noContent().build();
    }

}
