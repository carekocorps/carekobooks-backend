package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs.BookReviewControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookReviewResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookReviewQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookReviewService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.review.BookReviewNotFoundException;
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
@RequestMapping("/v1/books/reviews")
@Tag(name = "Book Review", description = "Endpoints for managing book reviews")
public class BookReviewController implements BaseController, BookReviewControllerDocs {

    private final BookReviewService bookReviewService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<BookReviewResponse>> search(@ParameterObject BookReviewQuery query) {
        var responses = bookReviewService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookReviewResponse> find(@PathVariable Long id) {
        var response = bookReviewService.find(id);
        return response.map(ResponseEntity::ok).orElseThrow(BookReviewNotFoundException::new);
    }

    @Override
    @RequireUserPermission
    @PostMapping
    public ResponseEntity<BookReviewResponse> create(@RequestBody @Valid BookReviewRequest request) {
        var response = bookReviewService.create(request);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @RequireUserPermission
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid BookReviewRequest request) {
        bookReviewService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookReviewService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
