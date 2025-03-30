package br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.image.infrastructure.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
