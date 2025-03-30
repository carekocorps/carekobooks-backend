package br.com.edu.ifce.maracanau.carekobooks.module.file.application.service;

import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.file.application.dto.StoredFileDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.file.application.mapper.StoredFileMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.file.infrastructure.model.StoredFile;
import br.com.edu.ifce.maracanau.carekobooks.module.file.infrastructure.repository.StoredFileRepository;
import br.com.edu.ifce.maracanau.carekobooks.shared.utils.MinioUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoredFileService {

    private final StoredFileRepository storedFileRepository;
    private final MinioUtils minioUtils;
    private final StoredFileMapper storedFileMapper;

    @Transactional
    public Optional<StoredFileDTO> findById(Long id) { return storedFileRepository.findById(id).map(storedFileMapper::toDTO); }

    @Transactional
    public StoredFileDTO create(MultipartFile file) throws Exception {
        var fileName = minioUtils.uploadFile(file);
        var url = minioUtils.getFileUrl(fileName);

        StoredFile newFile = new StoredFile(url, fileName);

        return storedFileMapper.toDTO(storedFileRepository.save(newFile));
    }

    @Transactional
    public void deleteById(Long id) throws Exception {
        var storedFile = storedFileRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("File not found"));

        minioUtils.deleteFile(storedFile.getFileName());
        storedFileRepository.deleteById(id);
    }

}
