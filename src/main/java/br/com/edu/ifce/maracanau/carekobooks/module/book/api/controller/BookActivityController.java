package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs.BookActivityControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookActivityFeedQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookActivityQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookActivityService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.activity.BookActivityNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.RequireUserPermission;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page.ApplicationPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/books/activities")
@Tag(name = "Book Activity", description = "Endpoints for managing book activities")
public class BookActivityController implements BaseController, BookActivityControllerDocs {

    private final BookActivityService bookActivityService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<BookActivityResponse>> search(@ParameterObject BookActivityQuery query) {
        var responses = bookActivityService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/social/feed")
    public ResponseEntity<ApplicationPage<BookActivityResponse>> search(@ParameterObject BookActivityFeedQuery query) {
        var responses = bookActivityService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookActivityResponse> find(@PathVariable Long id) {
        var response = bookActivityService.find(id);
        return response.map(ResponseEntity::ok).orElseThrow(BookActivityNotFoundException::new);
    }

    @Override
    @RequireUserPermission
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookActivityService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
