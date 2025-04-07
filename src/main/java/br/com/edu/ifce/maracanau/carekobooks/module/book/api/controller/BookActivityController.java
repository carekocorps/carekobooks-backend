package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs.BookActivityControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookActivitySearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookActivityService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.page.ApplicationPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/book-activities")
@Tag(name = "Book Activity", description = "Endpoints for managing book activities")
public class BookActivityController implements BaseController, BookActivityControllerDocs {

    private final BookActivityService bookActivityService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<BookActivityResponse>> search(@ParameterObject BookActivitySearchQuery query) {
        var responses = bookActivityService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookActivityResponse> findById(@PathVariable Long id) {
        var response = bookActivityService.findById(id);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookActivityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
