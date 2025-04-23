package br.com.edu.ifce.maracanau.carekobooks.module.forum.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.api.controller.docs.ForumControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.response.ForumResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.request.ForumRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.query.ForumQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.service.ForumService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/forums")
@Tag(name = "Forum", description = "Endpoints for managing forums")
public class ForumController implements BaseController, ForumControllerDocs {

    private final ForumService forumService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<ForumResponse>> search(@ParameterObject ForumQuery query) {
        var responses = forumService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ForumResponse> find(@PathVariable Long id) {
        var response = forumService.find(id);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @UserRoleRequired
    @PostMapping
    public ResponseEntity<ForumResponse> create(@RequestBody @Valid ForumRequest request) {
        var response = forumService.create(request);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @UserRoleRequired
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid ForumRequest request) {
        forumService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        forumService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
