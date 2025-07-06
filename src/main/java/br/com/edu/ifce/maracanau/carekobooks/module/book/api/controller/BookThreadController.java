package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.thread.BookThreadNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.RequireUserPermission;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs.BookThreadControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookThreadQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookThreadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/books/threads")
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
        return response.map(ResponseEntity::ok).orElseThrow(BookThreadNotFoundException::new);
    }

    @Override
    @RequireUserPermission
    @PostMapping
    public ResponseEntity<BookThreadResponse> create(@RequestBody @Valid BookThreadRequest request) {
        var response = bookThreadService.create(request);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @RequireUserPermission
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid BookThreadRequest request) {
        bookThreadService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookThreadService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
