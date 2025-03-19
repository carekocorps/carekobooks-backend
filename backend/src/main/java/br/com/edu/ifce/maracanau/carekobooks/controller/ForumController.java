package br.com.edu.ifce.maracanau.carekobooks.controller;

import br.com.edu.ifce.maracanau.carekobooks.controller.docs.ForumControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.core.page.ApplicationPage;
import br.com.edu.ifce.maracanau.carekobooks.dto.forum.ForumDTO;
import br.com.edu.ifce.maracanau.carekobooks.dto.forum.ForumPageQueryDTO;
import br.com.edu.ifce.maracanau.carekobooks.dto.forum.BookPageQueryDTO;
import br.com.edu.ifce.maracanau.carekobooks.service.ForumService;
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
    public ResponseEntity<ApplicationPage<ForumDTO>> search(@ParameterObject BookPageQueryDTO forumSearchDTO) {
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
    public ResponseEntity<ForumDTO> create(@RequestBody @Valid ForumPageQueryDTO forumPageQueryDTO) {
        var forumDTO = forumService.create(forumPageQueryDTO);
        var uri = getHeaderLocation(forumDTO.getId());
        return ResponseEntity.created(uri).body(forumDTO);
    }

    @PutMapping("/{id}")
    @Override
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody ForumPageQueryDTO forumPageQueryDTO) {
        forumService.update(id, forumPageQueryDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        forumService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
