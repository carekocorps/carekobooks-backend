package br.com.edu.ifce.maracanau.carekobooks.module.book.infra.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infra.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    boolean existsById(Long id);

}
