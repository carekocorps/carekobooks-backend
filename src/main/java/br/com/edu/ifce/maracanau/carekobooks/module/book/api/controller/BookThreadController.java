package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs.BookThreadControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookThreadResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookThreadRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookThreadQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookThreadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books/threads")
@Tag(name = "Book Thread", description = "Endpoints for managing threads")
public class BookThreadController implements BaseController, BookThreadControllerDocs {

    private final BookThreadService bookThreadService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<BookThreadResponse>> search(@ParameterObject BookThreadQuery query) {
        var responses = bookThreadService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookThreadResponse> find(@PathVariable Long id) {
        var response = bookThreadService.find(id);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @UserRoleRequired
    @PostMapping
    public ResponseEntity<BookThreadResponse> create(@RequestBody @Valid BookThreadRequest request) {
        var response = bookThreadService.create(request);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @UserRoleRequired
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid BookThreadRequest request) {
        bookThreadService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookThreadService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
