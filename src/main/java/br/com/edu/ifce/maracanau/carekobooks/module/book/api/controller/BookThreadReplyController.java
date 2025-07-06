package br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.book.api.controller.docs.BookThreadReplyControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadReplyResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.query.BookThreadReplyQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookThreadReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.BookThreadReplyService;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.exception.thread.reply.BookThreadReplyNotFoundException;
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
@RequestMapping("/v1/books/threads/replies")
@Tag(name = "Book Thread Reply", description = "Endpoints for managing thread replies")
public class BookThreadReplyController implements BaseController, BookThreadReplyControllerDocs {

    private final BookThreadReplyService bookThreadReplyService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<BookThreadReplyResponse>> search(@ParameterObject BookThreadReplyQuery query) {
        var responses = bookThreadReplyService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BookThreadReplyResponse> find(@PathVariable Long id) {
        var response = bookThreadReplyService.find(id);
        return response.map(ResponseEntity::ok).orElseThrow(BookThreadReplyNotFoundException::new);
    }

    @Override
    @RequireUserPermission
    @PostMapping
    public ResponseEntity<BookThreadReplyResponse> create(@RequestBody @Valid BookThreadReplyRequest request) {
        var response = bookThreadReplyService.create(request);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @RequireUserPermission
    @PostMapping("/{id}/children")
    public ResponseEntity<BookThreadReplyResponse> addChild(@PathVariable Long id, @RequestBody @Valid BookThreadReplyRequest request) {
        var response = bookThreadReplyService.createChild(id, request);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @RequireUserPermission
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid BookThreadReplyRequest request) {
        bookThreadReplyService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @RequireUserPermission
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookThreadReplyService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
