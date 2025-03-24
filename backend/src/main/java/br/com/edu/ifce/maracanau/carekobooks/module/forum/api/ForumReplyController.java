package br.com.edu.ifce.maracanau.carekobooks.module.forum.api;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.api.docs.ForumReplyControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.dto.ForumReplyDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.query.ForumReplySearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.representation.request.ForumReplyRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.service.ForumReplyService;
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
@RequestMapping("/api/v1/forum-replies")
@Tag(name = "Forum Reply", description = "Endpoints for managing forum replies")
public class ForumReplyController implements BaseController, ForumReplyControllerDocs {

    private final ForumReplyService forumReplyService;

    @Override
    @GetMapping
    public ResponseEntity<ApplicationPage<ForumReplyDTO>> search(@ParameterObject ForumReplySearchQuery query) {
        var forumReplyDTOs = forumReplyService.search(query);
        return ResponseEntity.ok(forumReplyDTOs);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ForumReplyDTO> findById(@PathVariable Long id) {
        var forumReplyDTO = forumReplyService.findById(id);
        return forumReplyDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @UserRoleRequired
    @PostMapping
    public ResponseEntity<ForumReplyDTO> create(@RequestBody @Valid ForumReplyRequest request) {
        var forumReplyDTO = forumReplyService.create(request);
        var uri = getHeaderLocation(forumReplyDTO.getId());
        return ResponseEntity.created(uri).body(forumReplyDTO);
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
