package br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ForumReplyRepository extends JpaRepository<ForumReply, Long>, JpaSpecificationExecutor<ForumReply> {

    boolean existsById(Long id);

}
