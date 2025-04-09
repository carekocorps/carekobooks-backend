package br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller.docs.UserControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.UserQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.UserSocialQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.query.enums.UserRelationshipStatus;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserRegisterRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.UserService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.enums.ToggleAction;
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
        query.setStatus(UserRelationshipStatus.FOLLOWER);
        var responses = userService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{username}/following")
    public ResponseEntity<ApplicationPage<UserResponse>> searchFollowing(@PathVariable String username, @ParameterObject UserSocialQuery query) {
        query.setUsername(username);
        query.setStatus(UserRelationshipStatus.FOLLOWING);
        var responses = userService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> findByUsername(@PathVariable String username) {
        var response = userService.findByUsername(username);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @UserRoleRequired
    @PutMapping(value = "/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> update(@PathVariable String username, @ModelAttribute @Valid UserRegisterRequest request) throws Exception {
        userService.update(username, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @PostMapping("/{username}/following/{targetUsername}")
    public ResponseEntity<Void> assignFollowingByUsername(@PathVariable String username, @PathVariable String targetUsername) {
        userService.updateFollowingByUsername(username, targetUsername, ToggleAction.ASSIGN);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{username}/following/{targetUsername}")
    public ResponseEntity<Void> unassignFollowingByUsername(@PathVariable String username, @PathVariable String targetUsername) {
        userService.updateFollowingByUsername(username, targetUsername, ToggleAction.UNASSIGN);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @PostMapping(value = "/{username}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateImageByUsername(@PathVariable String username, @RequestParam MultipartFile image) throws Exception {
        userService.updateImageByUsername(username, image);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping(value = "/{username}/images")
    public ResponseEntity<Void> deleteImageByUsername(@PathVariable String username) throws Exception {
        userService.deleteImageByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }

}
