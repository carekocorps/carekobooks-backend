package br.com.edu.ifce.maracanau.carekobooks.module.forum.application.service.validator;

import br.com.edu.ifce.maracanau.carekobooks.common.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.service.validator.BaseValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model.ForumReply;
import org.springframework.stereotype.Component;

@Component
public class ForumReplyValidator implements BaseValidator<ForumReply> {

    public void validate(ForumReply forumReply) {
        if (isUserEmpty(forumReply)) {
            throw new NotFoundException("User not found");
        }

        if (isForumEmpty(forumReply)) {
            throw new NotFoundException("Forum not found");
        }
    }

    private boolean isUserEmpty(ForumReply forumReply) {
        return forumReply.getUser() == null;
    }

    private boolean isForumEmpty(ForumReply forumReply) {
        return forumReply.getForum() == null;
    }

}
