package br.com.edu.ifce.maracanau.carekobooks.repository;

import br.com.edu.ifce.maracanau.carekobooks.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
