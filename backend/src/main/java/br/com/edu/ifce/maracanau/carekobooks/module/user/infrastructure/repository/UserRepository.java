package br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository;

import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
