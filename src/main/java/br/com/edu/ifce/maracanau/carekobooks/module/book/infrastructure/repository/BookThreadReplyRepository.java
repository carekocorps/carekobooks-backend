package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookThreadReply;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookThreadReplyRepository extends JpaRepository<BookThreadReply, Long>, JpaSpecificationExecutor<BookThreadReply> {

    List<BookThreadReply> findByIdIn(List<Long> ids);
    boolean existsById(@NotNull Long id);

}
