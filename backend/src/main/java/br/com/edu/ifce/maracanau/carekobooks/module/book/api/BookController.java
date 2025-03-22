package br.com.edu.ifce.maracanau.carekobooks.module.book.api;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.shared.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.module.book.api.docs.BookControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.query.BookSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookService;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.ApplicationPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Book", description = "Endpoints for managing books")
public class BookController implements BaseController, BookControllerDocs {

    private final BookService bookService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<BookDTO>> search(@ParameterObject BookSearchQuery query) {
        var bookDTOs = bookService.search(query);
        return ResponseEntity.ok(bookDTOs);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findById(@PathVariable Long id) {
        var bookDTO = bookService.findById(id);
        return bookDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @PostMapping
    public ResponseEntity<BookDTO> create(@RequestBody @Valid BookRequest request) {
        var bookDTO = bookService.create(request);
        var uri = getHeaderLocation(bookDTO.getId());
        return ResponseEntity.created(uri).body(bookDTO);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody BookRequest request) {
        bookService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
