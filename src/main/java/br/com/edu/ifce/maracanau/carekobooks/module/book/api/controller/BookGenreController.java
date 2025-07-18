package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs.BookGenreControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookGenreQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookGenreResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookGenreService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.genre.BookGenreNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.RequireAdminPermission;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/books/genres")
@Tag(name = "Book Genre", description = "Endpoints for managing book genres")
public class BookGenreController implements BaseController, BookGenreControllerDocs {

    private final BookGenreService bookGenreService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<BookGenreResponse>> search(@ParameterObject BookGenreQuery query) {
        var responses = bookGenreService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{name}")
    public ResponseEntity<BookGenreResponse> find(@PathVariable String name) {
        var response = bookGenreService.find(name);
        return response.map(ResponseEntity::ok).orElseThrow(BookGenreNotFoundException::new);
    }

    @Override
    @RequireAdminPermission
    @PostMapping
    public ResponseEntity<BookGenreResponse> create(@RequestBody @Valid BookGenreRequest request) {
        var response = bookGenreService.create(request);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @RequireAdminPermission
    @PutMapping("/{name}")
    public ResponseEntity<Void> update(@PathVariable String name, @RequestBody @Valid BookGenreRequest request) {
        bookGenreService.update(name, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireAdminPermission
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@PathVariable String name) {
        bookGenreService.delete(name);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireAdminPermission
    @DeleteMapping("/cache")
    public ResponseEntity<Void> clearCache() {
        bookGenreService.clearCache();
        return ResponseEntity.noContent().build();
    }

}
