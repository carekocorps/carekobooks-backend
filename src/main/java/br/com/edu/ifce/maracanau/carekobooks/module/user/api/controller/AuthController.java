package br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller.docs.AuthControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.*;
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
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid UserLoginRequest request, HttpServletResponse response) {
        authService.login(request, response);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/refresh/{username}")
    public ResponseEntity<Void> refreshToken(@PathVariable String username, HttpServletRequest request, HttpServletResponse response) {
        authService.refreshToken(username, request, response);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/otp")
    public ResponseEntity<Void> generateOtp(@RequestBody UserGenerateOtpRequest request) {
        authService.generateOtp(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> register(@RequestPart @Valid UserRegisterRequest request, @RequestParam(required = false) MultipartFile image) {
        var response = authService.register(request, image);
        var uri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/users/{username}")
                .buildAndExpand(response.getUsername())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @Override
    @PostMapping("/register/otp")
    public ResponseEntity<Void> verify(@RequestBody @Valid UserRegisterVerificationRequest request) {
        authService.verify(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/password/otp")
    public ResponseEntity<Void> recoverPassword(@RequestBody @Valid UserRecoverPasswordRequest request) {
        authService.recoverPassword(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/email/otp")
    public ResponseEntity<Void> changeEmail(@RequestBody @Valid UserChangeEmailRequest request) {
        authService.changeEmail(request);
        return ResponseEntity.noContent().build();
    }

}
