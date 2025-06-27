package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookUpdateRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.simplified.SimplifiedBookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.book.BookNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.RequireAdminPermission;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.RequireUserPermission;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs.BookControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookService;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
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
    public ResponseEntity<ApplicationPage<SimplifiedBookResponse>> search(@ParameterObject BookQuery query) {
        var responses = bookService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> find(@PathVariable Long id) {
        var response = bookService.find(id);
        return response.map(ResponseEntity::ok).orElseThrow(BookNotFoundException::new);
    }

    @Override
    @RequireAdminPermission
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BookResponse> create(@RequestPart @Valid BookRequest request, @RequestPart(required = false) MultipartFile image) {
        var response = bookService.create(request, image);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @RequireAdminPermission
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestPart @Valid BookUpdateRequest request, @RequestParam(required = false) MultipartFile image) {
        bookService.update(id, request, image);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireAdminPermission
    @PostMapping("/{id}/genres/{genreName}")
    public ResponseEntity<Void> assignGenre(@PathVariable Long id, @PathVariable String genreName) {
        bookService.changeGenre(id, genreName, true);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireAdminPermission
    @DeleteMapping("/{id}/genres/{genreName}")
    public ResponseEntity<Void> unassignGenre(@PathVariable Long id, @PathVariable String genreName) {
        bookService.changeGenre(id, genreName, false);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @PostMapping(value = "/{id}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> assignImage(@PathVariable Long id, @RequestParam(required = false) MultipartFile image) {
        bookService.changeImage(id, image);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @DeleteMapping(value = "/{id}/images")
    public ResponseEntity<Void> unassignImage(@PathVariable Long id) {
        bookService.changeImage(id, null);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireAdminPermission
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireAdminPermission
    @DeleteMapping("/cache")
    public ResponseEntity<Void> clearCache() {
        bookService.clearCache();
        return ResponseEntity.noContent().build();
    }

}
