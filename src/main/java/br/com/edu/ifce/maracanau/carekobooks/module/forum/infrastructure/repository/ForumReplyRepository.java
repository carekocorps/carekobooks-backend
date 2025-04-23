package br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumReplyRepository extends JpaRepository<ForumReply, Long>, JpaSpecificationExecutor<ForumReply> {

    boolean existsById(@NotNull Long id);

}
