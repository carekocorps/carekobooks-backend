package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookProgressRepository extends JpaRepository<BookProgress, Long>, JpaSpecificationExecutor<BookProgress> {

    @Transactional
    @Modifying
    @Query("UPDATE BookProgress bp SET bp.isFavorited = :isFavorited WHERE bp.id = :id")
    void updateIsFavoritedById(Boolean isFavorited, Long id);

}
