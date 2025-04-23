package br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller.docs.AuthControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.TokenResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.AuthService;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> initRegistration(@RequestPart @Valid UserRegisterInitializationRequest request, @RequestParam(required = false) MultipartFile image) {
        var response = authService.initRegistration(request, image);
        var uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{username}")
                .buildAndExpand(response.getUsername())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Override
    @PostMapping("/register/verify")
    public ResponseEntity<Void> verifyRegistration(@RequestBody @Valid UserRegisterVerificationRequest request) {
        authService.verifyRegistration(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> initPasswordRecovery(@RequestBody @Valid UserPasswordRecoveryInitializationRequest request) {
        authService.initPasswordRecovery(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/forgot-password/verify")
    public ResponseEntity<Void> verifyPasswordRecovery(@RequestBody @Valid UserPasswordRecoveryVerificationRequest request) {
        authService.verifyPasswordRecovery(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/change-email")
    public ResponseEntity<Void> initEmailChange(@RequestBody @Valid UserEmailChangeInitializationRequest request) {
        authService.initEmailChange(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/change-email/verify")
    public ResponseEntity<Void> verifyEmailChange(@RequestBody @Valid UserEmailChangeVerificationRequest request) {
        authService.verifyEmailChange(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/refresh/{username}")
    public ResponseEntity<TokenResponse> refreshToken(@PathVariable String username, @RequestBody @Valid UserTokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(username, request));
    }

}
