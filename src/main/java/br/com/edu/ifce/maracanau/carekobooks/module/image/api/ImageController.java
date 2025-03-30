package br.com.edu.ifce.maracanau.carekobooks.module.image.api;

import br.com.edu.ifce.maracanau.carekobooks.module.image.api.docs.ImageControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.dto.ImageDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.AdminRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.UserRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.shared.layer.api.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/images")
@Tag(name = "Image", description = "Endpoints for managing images")
public class ImageController implements BaseController, ImageControllerDocs {

    private final ImageService imageService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> findById(@PathVariable Long id) {
        var imageDTO = imageService.findById(id);
        return imageDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @UserRoleRequired
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDTO> create(@RequestParam MultipartFile file) throws Exception {
        var imageDTO = imageService.create(file);
        var uri = getHeaderLocation(imageDTO.getId());
        return ResponseEntity.created(uri).body(imageDTO);
    }

    @Override
    @AdminRoleRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws Exception {
        imageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
