package br.com.edu.ifce.maracanau.carekobooks.module.image.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.validator.ImageValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.model.Image;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final MinioService minioService;

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final ImageValidator imageValidator;

    public Optional<ImageResponse> find(Long id) {
        return imageRepository.findById(id).map(imageMapper::toResponse);
    }

    @Transactional
    public ImageResponse create(MultipartFile file) {
        var image = new Image();
        image.setName(minioService.upload(file));
        image.setUrl(minioService.findUrlByFilename(image.getName()));
        image.setContentType(file.getContentType());
        image.setSizeInBytes(file.getSize());
        imageValidator.validate(image);
        return imageMapper.toResponse(imageRepository.save(image));
    }

    @Transactional
    public void delete(Long id) {
        var image = imageRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("File not found"));

        minioService.deleteByFilename(image.getName());
        imageRepository.deleteById(id);
    }

}
