package br.com.edu.ifce.maracanau.carekobooks.module.book.api;

import br.com.edu.ifce.maracanau.carekobooks.module.book.api.docs.BookReviewControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto.BookReviewDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookReviewSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookReviewRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookReviewService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.shared.api.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.ApplicationPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/book-reviews")
@Tag(name = "Book Review", description = "Endpoints for managing book reviews")
public class BookReviewController implements BaseController, BookReviewControllerDocs {

    private final BookReviewService bookReviewService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<BookReviewDTO>> search(@ParameterObject BookReviewSearchQuery query) {
        var bookReviewDTOs = bookReviewService.search(query);
        return ResponseEntity.ok(bookReviewDTOs);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookReviewDTO> findById(@PathVariable Long id) {
        var bookReviewDTO = bookReviewService.findById(id);
        return bookReviewDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @UserRoleRequired
    @PostMapping
    public ResponseEntity<BookReviewDTO> create(@RequestBody @Valid BookReviewRequest request) {
        var bookReviewDTO = bookReviewService.create(request);
        var uri = getHeaderLocation(bookReviewDTO.getId());
        return ResponseEntity.created(uri).body(bookReviewDTO);
    }

    @Override
    @UserRoleRequired
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid BookReviewRequest request) {
        bookReviewService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookReviewService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
