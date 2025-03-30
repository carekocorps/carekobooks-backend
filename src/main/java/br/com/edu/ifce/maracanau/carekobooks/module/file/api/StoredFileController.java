package br.com.edu.ifce.maracanau.carekobooks.module.file.api;

import br.com.edu.ifce.maracanau.carekobooks.module.file.api.docs.StoredFileControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.file.application.dto.StoredFileDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.file.application.service.StoredFileService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.AdminRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.shared.api.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/files")
@Tag(name = "File", description = "Endpoints for managing files")
public class StoredFileController implements BaseController, StoredFileControllerDocs {

    private final StoredFileService storedFileService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<StoredFileDTO> findById(@PathVariable Long id) {
        var storedFileDTO = storedFileService.findById(id);
        return storedFileDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @AdminRoleRequired
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StoredFileDTO> create(@RequestParam("file") MultipartFile file) throws Exception {
        var storedFileDTO = storedFileService.create(file);
        var uri = getHeaderLocation(storedFileDTO.getId());
        return ResponseEntity.created(uri).body(storedFileDTO);
    }

    @Override
    //@AdminRoleRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws Exception {
        storedFileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
