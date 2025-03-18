package br.com.edu.ifce.maracanau.carekobooks.controller;

import br.com.edu.ifce.maracanau.carekobooks.controller.docs.BookControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookDTO;
import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookRequestDTO;
import br.com.edu.ifce.maracanau.carekobooks.dto.book.BookSearchDTO;
import br.com.edu.ifce.maracanau.carekobooks.service.BookService;
import br.com.edu.ifce.maracanau.carekobooks.core.page.ApplicationPage;
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
public class BookController extends BaseController implements BookControllerDocs {

    private final BookService bookService;

    @GetMapping
    @Override
    public ResponseEntity<ApplicationPage<BookDTO>> search(@ParameterObject BookSearchDTO bookSearchDTO) {
        var bookDTOs = bookService.search(bookSearchDTO);
        return ResponseEntity.ok(bookDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findById(@PathVariable Long id) {
        var bookDTO = bookService.findById(id);
        return bookDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Override
    public ResponseEntity<BookDTO> create(@RequestBody @Valid BookRequestDTO bookRequestDTO) {
        var bookDTO = bookService.create(bookRequestDTO);
        var uri = getHeaderLocation(bookDTO.getId());
        return ResponseEntity.created(uri).body(bookDTO);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody BookRequestDTO bookRequestDTO) {
        bookService.update(id, bookRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
