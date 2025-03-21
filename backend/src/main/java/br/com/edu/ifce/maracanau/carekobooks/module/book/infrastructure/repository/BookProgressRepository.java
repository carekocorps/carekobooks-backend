package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookProgressRepository extends JpaRepository<BookProgress, Long>, JpaSpecificationExecutor<BookProgress> {
}
