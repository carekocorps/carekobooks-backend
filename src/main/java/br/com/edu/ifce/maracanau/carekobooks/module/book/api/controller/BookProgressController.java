package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs.BookProgressControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookProgressResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookProgressQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookProgressService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.enums.ToggleAction;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/book-progresses")
@Tag(name = "Book Progress", description = "Endpoints for managing book progresses")
public class BookProgressController implements BaseController, BookProgressControllerDocs {

    private final BookProgressService bookProgressService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<BookProgressResponse>> search(@ParameterObject BookProgressQuery query) {
        var responses = bookProgressService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookProgressResponse> findById(@PathVariable Long id) {
        var response = bookProgressService.findById(id);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @UserRoleRequired
    @PostMapping
    public ResponseEntity<BookProgressResponse> create(@RequestBody @Valid BookProgressRequest request) {
        var response = bookProgressService.create(request);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @UserRoleRequired
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid BookProgressRequest request) {
        bookProgressService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @PostMapping("/{id}/favorites")
    public ResponseEntity<Void> assignAsFavoriteById(@PathVariable Long id) {
        bookProgressService.updateIsFavoriteById(id, ToggleAction.ASSIGN);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{id}/favorites")
    public ResponseEntity<Void> unassignAsFavoriteById(@PathVariable Long id) {
        bookProgressService.updateIsFavoriteById(id, ToggleAction.UNASSIGN);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookProgressService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
