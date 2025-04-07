package br.com.edu.ifce.maracanau.carekobooks.module.forum.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.api.controller.docs.ForumReplyControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.response.ForumReplyResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.query.ForumReplySearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.request.ForumReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.service.ForumReplyService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.page.ApplicationPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/forum-replies")
@Tag(name = "Forum Reply", description = "Endpoints for managing forum replies")
public class ForumReplyController implements BaseController, ForumReplyControllerDocs {

    private final ForumReplyService forumReplyService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<ForumReplyResponse>> search(@ParameterObject ForumReplySearchQuery query) {
        var responses = forumReplyService.search(query);
        return ResponseEntity.ok(responses);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ForumReplyResponse> findById(@PathVariable Long id) {
        var response = forumReplyService.findById(id);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @UserRoleRequired
    @PostMapping
    public ResponseEntity<ForumReplyResponse> create(@RequestBody @Valid ForumReplyRequest request) {
        var response = forumReplyService.create(request);
        var uri = getHeaderLocation(response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @Override
    @UserRoleRequired
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid ForumReplyRequest request) {
        forumReplyService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @UserRoleRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        forumReplyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
