package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs.BookProgressControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookProgressResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookProgressQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookProgressService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.progress.BookProgressNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.RequireUserPermission;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/books/progresses")
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
    public ResponseEntity<BookProgressResponse> find(@PathVariable Long id) {
        var response = bookProgressService.find(id);
        return response.map(ResponseEntity::ok).orElseThrow(BookProgressNotFoundException::new);
    }

    @Override
    @RequireUserPermission
    @PostMapping
    public ResponseEntity<BookProgressResponse> create(@RequestBody @Valid BookProgressRequest request) {
        var response = bookProgressService.create(request);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity
                .created(uri)
                .header("X-BookActivity-Created", "true")
                .body(response);
    }

    @Override
    @RequireUserPermission
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid BookProgressRequest request) {
        bookProgressService.update(id, request);
        return ResponseEntity
                .noContent()
                .header("X-BookActivity-Created", "true")
                .build();
    }

    @Override
    @RequireUserPermission
    @PostMapping("/{id}/favorites")
    public ResponseEntity<Void> assignAsFavorite(@PathVariable Long id) {
        bookProgressService.changeAsFavorite(id, true);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @DeleteMapping("/{id}/favorites")
    public ResponseEntity<Void> unassignAsFavorite(@PathVariable Long id) {
        bookProgressService.changeAsFavorite(id, false);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookProgressService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
