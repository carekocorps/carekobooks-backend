package br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller.docs.AuthControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.TokenResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.AuthService;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Endpoints for managing user authentication")
public class AuthController implements BaseController, AuthControllerDocs {

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid UserLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid UserRegistrationRequest request) {
        var response = authService.register(request);
        var uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{username}")
                .buildAndExpand(response.getUsername())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Override
    @PostMapping("/verify")
    public ResponseEntity<Void> verify(@RequestBody @Valid UserVerificationRequest request) {
        authService.verify(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(UserPasswordRecoveryRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(UserPasswordResetRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/refresh/{username}")
    public ResponseEntity<TokenResponse> refresh(@PathVariable String username, @RequestBody @Valid UserTokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(username, request));
    }

}
