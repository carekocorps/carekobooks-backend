package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookThreadReplyResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookThreadReplyQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookThreadReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
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

public interface BookThreadReplyControllerDocs {

    @Operation(
            summary = "Search all thread replies",
            tags = {"Thread Reply"},
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
    ResponseEntity<ApplicationPage<BookThreadReplyResponse>> search(@ParameterObject BookThreadReplyQuery query);

    @Operation(
            summary = "Find a thread reply by ID",
            tags = {"Thread Reply"},
            responses = {
                    @ApiResponse(
                            description = "Ok",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookThreadReplyResponse.class)
                            )
                    ),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<BookThreadReplyResponse> findById(@PathVariable Long id);

    @Operation(
            summary = "Create a thread reply",
            tags = {"Thread Reply"},
            security = @SecurityRequirement(name = "Bearer"),
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookThreadReplyResponse.class)
                            )
                    ),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<BookThreadReplyResponse> create(@RequestBody @Valid BookThreadReplyRequest request);

    @Operation(
            summary = "Update a thread reply",
            tags = {"Thread Reply"},
            security = @SecurityRequirement(name = "Bearer"),
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid BookThreadReplyRequest request);

    @Operation(
            summary = "Delete a thread reply by ID",
            tags = {"Thread Reply"},
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
