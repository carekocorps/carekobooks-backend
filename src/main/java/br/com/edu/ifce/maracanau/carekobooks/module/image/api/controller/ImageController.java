package br.com.edu.ifce.maracanau.carekobooks.module.image.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.image.api.controller.docs.ImageControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.security.annotation.AdminRoleRequired;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/images")
@Tag(name = "Image", description = "Endpoints for managing images")
public class ImageController implements BaseController, ImageControllerDocs {

    private final ImageService imageService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ImageResponse> findById(@PathVariable Long id) {
        var response = imageService.findById(id);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    @AdminRoleRequired
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) throws Exception {
        imageService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
