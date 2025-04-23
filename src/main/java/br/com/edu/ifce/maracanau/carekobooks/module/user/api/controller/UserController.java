package br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller.docs.UserControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.UserQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.UserSocialQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.enums.UserRelationship;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.UserService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.enums.ActionType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "Endpoints for managing users")
public class UserController implements BaseController, UserControllerDocs {

    private final UserService userService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<UserResponse>> search(@ParameterObject UserQuery query) {
        var responses = userService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{username}/followers")
    public ResponseEntity<ApplicationPage<UserResponse>> searchFollowers(@PathVariable String username, @ParameterObject UserSocialQuery query) {
        query.setUsername(username);
        query.setRelationship(UserRelationship.FOLLOWER);
        var responses = userService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{username}/following")
    public ResponseEntity<ApplicationPage<UserResponse>> searchFollowing(@PathVariable String username, @ParameterObject UserSocialQuery query) {
        query.setUsername(username);
        query.setRelationship(UserRelationship.FOLLOWING);
        var responses = userService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> find(@PathVariable String username) {
        var response = userService.find(username);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @UserRoleRequired
    @PutMapping(value = "/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> update(@PathVariable String username, @RequestPart @Valid UserUpdateRequest request, @RequestParam(required = false) MultipartFile image) throws Exception {
        userService.update(username, request, image);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @PostMapping("/{username}/following/{targetUsername}")
    public ResponseEntity<Void> follow(@PathVariable String username, @PathVariable String targetUsername) {
        userService.changeFollowing(username, targetUsername, ActionType.ASSIGN);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{username}/following/{targetUsername}")
    public ResponseEntity<Void> unfollow(@PathVariable String username, @PathVariable String targetUsername) {
        userService.changeFollowing(username, targetUsername, ActionType.UNASSIGN);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @PostMapping(value = "/{username}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> assignImage(@PathVariable String username, @RequestParam MultipartFile image) throws Exception {
        userService.changeImage(username, image);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping(value = "/{username}/images")
    public ResponseEntity<Void> unassignImage(@PathVariable String username) throws Exception {
        userService.changeImage(username, null);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        userService.delete(username);
        return ResponseEntity.noContent().build();
    }

}
