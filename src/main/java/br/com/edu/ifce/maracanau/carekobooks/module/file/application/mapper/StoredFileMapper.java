package br.com.edu.ifce.maracanau.carekobooks.module.file.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.file.application.dto.StoredFileDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.file.infrastructure.model.StoredFile;
import br.com.edu.ifce.maracanau.carekobooks.module.file.infrastructure.repository.StoredFileRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class StoredFileMapper {

    @Autowired
    protected StoredFileRepository storedFileRepository;

    public abstract StoredFile toModel(StoredFileDTO dto);
    public abstract StoredFileDTO toDTO(StoredFile file);

    public StoredFile toModel(Long id) { return storedFileRepository.findById(id).orElse(null); }

}
