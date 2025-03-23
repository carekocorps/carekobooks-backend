package br.com.edu.ifce.maracanau.carekobooks.module.forum.api;

import br.com.edu.ifce.maracanau.carekobooks.module.user.shared.annotation.HasUserRole;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.api.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.api.docs.ForumControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto.ForumDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.request.ForumRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.query.ForumSearchQuery;
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
    public ResponseEntity<ApplicationPage<ForumDTO>> search(@ParameterObject ForumSearchQuery query) {
        var forumDTOs = forumService.search(query);
        return ResponseEntity.ok(forumDTOs);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ForumDTO> findById(@PathVariable Long id) {
        var forumDTO = forumService.findById(id);
        return forumDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @HasUserRole
    @PostMapping
    public ResponseEntity<ForumDTO> create(@RequestBody @Valid ForumRequest request) {
        var forumDTO = forumService.create(request);
        var uri = getHeaderLocation(forumDTO.getId());
        return ResponseEntity.created(uri).body(forumDTO);
    }

    @Override
    @HasUserRole
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid ForumRequest request) {
        forumService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @Override
    @HasUserRole
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        forumService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
