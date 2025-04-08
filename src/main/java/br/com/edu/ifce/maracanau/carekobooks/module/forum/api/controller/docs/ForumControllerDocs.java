package br.com.edu.ifce.maracanau.carekobooks.module.forum.api.controller.docs;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.response.ForumResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.request.ForumRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.query.ForumSearchQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ForumControllerDocs {

    @Operation(
            summary = "Search all forums",
            tags = {"Forum"},
            responses = {
                    @ApiResponse(
                            description = "Ok",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ApplicationPage.class)
                            )
                    ),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<ApplicationPage<ForumResponse>> search(@ParameterObject ForumSearchQuery query);

    @Operation(
            summary = "Find a forum by ID",
            tags = {"Forum"},
            responses = {
                    @ApiResponse(
                            description = "Ok",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ForumResponse.class)
                            )
                    ),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<ForumResponse> findById(@PathVariable Long id);

    @Operation(
            summary = "Create a forum",
            tags = {"Forum"},
            security = @SecurityRequirement(name = "Bearer"),
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ForumResponse.class)
                            )
                    ),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<ForumResponse> create(@RequestBody @Valid ForumRequest request);

    @Operation(
            summary = "Update a forum",
            tags = {"Forum"},
            security = @SecurityRequirement(name = "Bearer"),
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid ForumRequest request);

    @Operation(
            summary = "Delete a forum by ID",
            tags = {"Forum"},
            security = @SecurityRequirement(name = "Bearer"),
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> deleteById(@PathVariable Long id);

}
