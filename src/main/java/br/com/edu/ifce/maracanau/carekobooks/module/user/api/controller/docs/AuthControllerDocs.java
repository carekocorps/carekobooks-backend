package br.com.edu.ifce.maracanau.carekobooks.module.user.api.controller.docs;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.payload.request.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

public interface AuthControllerDocs {

    @Operation(
            summary = "Authenticate a user",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204",content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> login(@RequestBody @Valid UserLoginRequest request, HttpServletResponse response);

    @Operation(
            summary = "Refresh a user authentication token",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204",content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> refreshToken(HttpServletRequest request, HttpServletResponse response);

    @Operation(
            summary = "Generate One-Time Password (OTP)",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204",content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> generateOtp(@RequestBody UserGenerateOtpRequest request);

    @Operation(
            summary = "Register a user",
            tags = {"Auth"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            encoding = @Encoding(
                                    name = "request",
                                    contentType = "application/json"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> register(@RequestPart @Valid UserRegisterRequest request, @RequestParam(required = false) MultipartFile image);

    @Operation(
            summary = "Verify a user's account",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204",content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> verify(@RequestBody @Valid UserRegisterVerificationRequest request);

    @Operation(
            summary = "Change a user's password",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204",content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> recoverPassword(UserRecoverPasswordRequest request);

    @Operation(
            summary = "Change a user's email",
            tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204",content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> changeEmail(@RequestBody @Valid UserChangeEmailRequest request);

    @Operation(
            summary = "Change a user's username",
            tags = {"Auth"},
            security = @SecurityRequirement(name = "access_token"),
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204",content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> changeUsername(@RequestBody @Valid UserChangeUsernameRequest request, HttpServletResponse response);

}
