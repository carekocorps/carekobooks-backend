package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.validator;

import br.com.edu.ifce.maracanau.carekobooks.module.forum.infra.model.Forum;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infra.repository.ForumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ForumValidator {

    private final ForumRepository forumRepository;

    public void validate(Forum forum) {

    }

}
