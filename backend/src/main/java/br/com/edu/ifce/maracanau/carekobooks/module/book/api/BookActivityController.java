package br.com.edu.ifce.maracanau.carekobooks.module.book.api;

import br.com.edu.ifce.maracanau.carekobooks.module.book.api.docs.BookActivityControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto.BookActivityDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.query.BookActivitySearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookActivityService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.shared.annotation.HasUserRole;
import br.com.edu.ifce.maracanau.carekobooks.shared.api.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.ApplicationPage;
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
    public ResponseEntity<ApplicationPage<BookActivityDTO>> search(@ParameterObject BookActivitySearchQuery query) {
        var bookActivityDTOs = bookActivityService.search(query);
        return ResponseEntity.ok(bookActivityDTOs);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookActivityDTO> findById(@PathVariable Long id) {
        var bookActivityDTO = bookActivityService.findById(id);
        return bookActivityDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @HasUserRole
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        bookActivityService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
