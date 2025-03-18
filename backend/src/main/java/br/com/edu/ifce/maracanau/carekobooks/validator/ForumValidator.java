package br.com.edu.ifce.maracanau.carekobooks.validator;

import br.com.edu.ifce.maracanau.carekobooks.model.Forum;
import br.com.edu.ifce.maracanau.carekobooks.repository.ForumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ForumValidator {

    private final ForumRepository forumRepository;

    public void validate(Forum forum) {

    }

}
