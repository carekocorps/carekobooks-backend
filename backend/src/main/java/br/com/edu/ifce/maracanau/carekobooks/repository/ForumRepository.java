package br.com.edu.ifce.maracanau.carekobooks.repository;

import br.com.edu.ifce.maracanau.carekobooks.model.Forum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long>, JpaSpecificationExecutor<Forum> {

    boolean existsById(Long id);

}
