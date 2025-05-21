package br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller.docs.AuthControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.*;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.AuthService;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    @PostMapping("/signin")
    public ResponseEntity<Void> signin(@RequestBody @Valid UserSignInRequest request, HttpServletResponse response) {
        authService.signin(request, response);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        authService.refreshToken(request, response);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> signup(@RequestPart @Valid UserSignUpRequest request, @RequestParam(required = false) MultipartFile image) {
        var response = authService.signup(request, image);
        var uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{username}")
                .buildAndExpand(response.getUsername())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Override
    @PostMapping("/otp")
    public ResponseEntity<Void> verifyOtp(@RequestBody @Valid UserOtpVerificationRequest request) {
        authService.verifyOtp(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @PostMapping("/password-recovery")
    public ResponseEntity<Void> recoverPassword(@RequestBody @Valid UserPasswordRecoveryRequest request) {
        authService.recoverPassword(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @PostMapping("/change-email")
    public ResponseEntity<Void> changeEmail(@RequestBody @Valid UserEmailChangeRequest request) {
        authService.changeEmail(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @PostMapping("/change-username")
    public ResponseEntity<Void> changeUsername(@RequestBody @Valid UserUsernameChangeRequest request, HttpServletResponse response) {
        authService.changeUsername(request, response);
        return ResponseEntity.noContent().build();
    }

}
