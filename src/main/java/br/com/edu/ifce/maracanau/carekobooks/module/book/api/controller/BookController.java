package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.AdminRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs.BookControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookService;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.enums.ToggleAction;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Book", description = "Endpoints for managing books")
public class BookController implements BaseController, BookControllerDocs {

    private final BookService bookService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<BookResponse>> search(@ParameterObject BookSearchQuery query) {
        var responses = bookService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> findById(@PathVariable Long id) {
        var response = bookService.findById(id);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @AdminRoleRequired
    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestBody @Valid BookRequest request) {
        var response = bookService.create(request);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @AdminRoleRequired
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid BookRequest request) {
        bookService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @AdminRoleRequired
    @PostMapping("/{id}/genres/{genreName}")
    public ResponseEntity<Void> assignGenreById(@PathVariable Long id, @PathVariable String genreName) {
        bookService.updateGenreById(id, genreName, ToggleAction.ASSIGN);
        return ResponseEntity.noContent().build();
    }

    @Override
    @AdminRoleRequired
    @DeleteMapping("/{id}/genres/{genreName}")
    public ResponseEntity<Void> unassignGenreById(@PathVariable Long id, @PathVariable String genreName) {
        bookService.updateGenreById(id, genreName, ToggleAction.UNASSIGN);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateImageById(@PathVariable Long id, @RequestParam MultipartFile image) throws Exception {
        bookService.updateImageById(id, image);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping(value = "/{id}/images")
    public ResponseEntity<Void> deleteImageById(@PathVariable Long id) throws Exception {
        bookService.deleteImageById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @AdminRoleRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
