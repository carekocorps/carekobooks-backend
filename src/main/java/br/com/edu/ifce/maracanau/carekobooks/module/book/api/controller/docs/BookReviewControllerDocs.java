package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookReviewResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookReviewSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.page.ApplicationPage;
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

public interface BookReviewControllerDocs {

    @Operation(
            summary = "Search all book reviews",
            tags = {"Book Review"},
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
    ResponseEntity<ApplicationPage<BookReviewResponse>> search(@ParameterObject BookReviewSearchQuery query);

    @Operation(
            summary = "Find a book review by ID",
            tags = {"Book Review"},
            responses = {
                    @ApiResponse(
                            description = "Ok",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookReviewResponse.class)
                            )
                    ),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<BookReviewResponse> findById(@PathVariable Long id);

    @Operation(
            summary = "Create a book review",
            tags = {"Book Review"},
            security = @SecurityRequirement(name = "Bearer"),
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = BookReviewResponse.class)
                            )
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Conflict", responseCode = "409", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<BookReviewResponse> create(@RequestBody @Valid BookReviewRequest request);

    @Operation(
            summary = "Update a book review",
            tags = {"Book Review"},
            security = @SecurityRequirement(name = "Bearer"),
            responses = {
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid BookReviewRequest request);

    @Operation(
            summary = "Delete a book review by ID",
            tags = {"Book Review"},
            security = @SecurityRequirement(name = "Bearer"),
            responses = {
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> deleteById(@PathVariable Long id);

}
