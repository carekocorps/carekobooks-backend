package br.com.edu.ifce.maracanau.carekobooks.module.image.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.exception.ImageNotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.payload.response.ImageResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.validator.ImageValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.domain.entity.Image;
import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final MinioService minioService;

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final ImageValidator imageValidator;

    @Transactional(readOnly = true)
    public Optional<ImageResponse> find(Long id) {
        return imageRepository.findById(id).map(imageMapper::toResponse);
    }

    @Transactional
    public ImageResponse create(MultipartFile file) {
        var image = new Image();
        image.setName(minioService.create(file));
        image.setContentType(file.getContentType());
        image.setSizeInBytes(file.getSize());
        imageValidator.validate(image);
        return imageMapper.toResponse(imageRepository.save(image));
    }

    @Transactional
    public void delete(Long id) {
        var image = imageRepository
                .findById(id)
                .orElseThrow(ImageNotFoundException::new);

        minioService.delete(image.getName());
        imageRepository.delete(image);
    }

}
