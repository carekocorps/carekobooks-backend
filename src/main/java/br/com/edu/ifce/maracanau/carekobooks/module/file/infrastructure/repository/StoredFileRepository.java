package br.com.edu.ifce.maracanau.carekobooks.module.file.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.file.infrastructure.model.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StoredFileRepository extends JpaRepository<StoredFile, Long>, JpaSpecificationExecutor<StoredFile> {
}
