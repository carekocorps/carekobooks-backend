package br.com.edu.ifce.maracanau.carekobooks.module.forum.api;

import br.com.edu.ifce.maracanau.carekobooks.shared.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.api.docs.ForumControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto.ForumDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.application.dto.ForumRequestDTO;
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
@RequestMapping("/api/v1/forum")
@Tag(name = "Forum", description = "Endpoints for managing forums")
public class ForumController extends BaseController implements ForumControllerDocs {

    private final ForumService forumService;

    @GetMapping
    @Override
    public ResponseEntity<ApplicationPage<ForumDTO>> search(@ParameterObject ForumSearchQuery forumSearchDTO) {
        var forumDTOs = forumService.search(forumSearchDTO);
        return ResponseEntity.ok(forumDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForumDTO> findById(@PathVariable Long id) {
        var forumDTO = forumService.findById(id);
        return forumDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Override
    public ResponseEntity<ForumDTO> create(@RequestBody @Valid ForumRequestDTO forumRequestDTO) {
        var forumDTO = forumService.create(forumRequestDTO);
        var uri = getHeaderLocation(forumDTO.getId());
        return ResponseEntity.created(uri).body(forumDTO);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody ForumRequestDTO forumRequestDTO) {
        forumService.update(id, forumRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        forumService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
