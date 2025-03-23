package br.com.edu.ifce.maracanau.carekobooks.module.forum.infrastructure.model;

import br.com.edu.ifce.maracanau.carekobooks.shared.module.infrastructure.model.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "forum_reply")
public class ForumReply extends BaseModel {

    @Column(length = 1000, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "forum_id", nullable = false)
    private Forum forum;

}
