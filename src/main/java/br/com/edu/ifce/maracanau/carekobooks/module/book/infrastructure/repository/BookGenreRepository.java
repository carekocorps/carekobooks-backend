package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, Long>, JpaSpecificationExecutor<BookGenre> {

    List<BookGenre> findAllByNameIn(List<String> names);
    Optional<BookGenre> findByName(String name);
    boolean existsByName(String name);
    void deleteByName(String name);

}
