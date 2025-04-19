package br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller.docs.AuthControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.RefreshTokenRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.response.TokenResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserLoginRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.representation.request.UserRegisterRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.service.AuthService;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/refresh/{username}")
    public ResponseEntity<TokenResponse> refresh(@PathVariable String username, @RequestBody @Valid RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(username, request.getRefreshToken()));
    }

    @Override
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

}
