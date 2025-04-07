package br.com.edu.ifce.maracanau.carekobooks.module.image.application.service;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.representation.dto.ImageDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.service.validator.ImageValidator;
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

    public Optional<ImageDTO> findById(Long id) {
        return imageRepository.findById(id).map(imageMapper::toDTO);
    }

    @Transactional
    public ImageDTO create(MultipartFile file) throws Exception {
        var image = imageMapper.toModel(file);
        imageValidator.validate(image);
        return imageMapper.toDTO(imageRepository.save(image));
    }

    @Transactional
    public void deleteById(Long id) throws Exception {
        var image = imageRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("File not found"));

        minioService.deleteByFilename(image.getName());
        imageRepository.deleteById(id);
    }

}
