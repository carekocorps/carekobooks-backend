package br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller.docs.UserControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserSignUpRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.UserUpdateUsernameRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.UserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.query.UserQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.response.simplified.SimplifiedUserResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.UserService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.RequireUserPermission;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.exception.user.UserNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "Endpoints for managing users")
public class UserController implements BaseController, UserControllerDocs {

    private final UserService userService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<SimplifiedUserResponse>> search(@ParameterObject UserQuery query) {
        var responses = userService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> find(@PathVariable String username) {
        var response = userService.find(username);
        return response.map(ResponseEntity::ok).orElseThrow(UserNotFoundException::new);
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> signUp(@RequestPart @Valid UserSignUpRequest request, @RequestParam(required = false) MultipartFile image) {
        var response = userService.signUp(request, image);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{username}")
                .buildAndExpand(response.getUsername())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @PostMapping("/{username}/reset-verification-email")
    public ResponseEntity<Void> resetVerificationEmail(@PathVariable String username) {
        userService.resetVerificationEmail(username);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @PostMapping("/{username}/reset-email")
    public ResponseEntity<Void> changeEmail(@PathVariable String username) {
        userService.changeEmail(username);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @PostMapping(value = "/{username}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> assignImage(@PathVariable String username, @RequestParam(required = false) MultipartFile image) {
        userService.changeImage(username, image);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @PatchMapping("/{username}/username")
    public ResponseEntity<Void> changeUsername(@PathVariable String username, @RequestBody @Valid UserUpdateUsernameRequest request) {
        userService.changeUsername(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @PostMapping("/{username}/2fa")
    public ResponseEntity<Void> assign2FA(@PathVariable String username) {
        userService.change2FA(username, true);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @PutMapping(value = "/{username}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> update(@PathVariable String username, @RequestPart @Valid UserUpdateRequest request, @RequestParam(required = false) MultipartFile image) {
        userService.update(username, request, image);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @DeleteMapping(value = "/{username}/images")
    public ResponseEntity<Void> unassignImage(@PathVariable String username) {
        userService.changeImage(username, null);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @DeleteMapping("/{username}/2fa")
    public ResponseEntity<Void> unassign2FA(@PathVariable String username) {
        userService.change2FA(username, false);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        userService.delete(username);
        return ResponseEntity.noContent().build();
    }

}
