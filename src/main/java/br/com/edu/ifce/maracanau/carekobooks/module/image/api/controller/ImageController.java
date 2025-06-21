package br.com.edu.ifce.maracanau.carekobooks.module.image.api.controller;

import br.com.edu.ifce.maracanau.carekobooks.module.image.api.controller.docs.ImageControllerDocs;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.ImageService;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.api.controller.BaseController;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageNotFoundException;
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
    public ResponseEntity<ImageResponse> find(@PathVariable Long id) {
        var response = imageService.find(id);
        return response.map(ResponseEntity::ok).orElseThrow(ImageNotFoundException::new);
    }

}
