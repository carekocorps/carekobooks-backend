package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs.BookGenreControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookGenreResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookGenreService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.AdminRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/book-genres")
@Tag(name = "Book Genre", description = "Endpoints for managing book genres")
public class BookGenreController implements BaseController, BookGenreControllerDocs {

    private final BookGenreService bookGenreService;

    @Override
    @GetMapping("/{name}")
    public ResponseEntity<BookGenreResponse> findByName(@PathVariable String name) {
        var response = bookGenreService.findByName(name);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @AdminRoleRequired
    @PostMapping
    public ResponseEntity<BookGenreResponse> create(@RequestBody @Valid BookGenreRequest request) {
        var response = bookGenreService.create(request);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @AdminRoleRequired
    @PutMapping("/{name}")
    public ResponseEntity<Void> update(@PathVariable String name, @RequestBody @Valid BookGenreRequest request) {
        bookGenreService.update(name, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @AdminRoleRequired
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteByName(@PathVariable String name) {
        bookGenreService.deleteByName(name);
        return ResponseEntity.noContent().build();
    }

}
