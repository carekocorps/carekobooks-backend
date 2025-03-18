package br.com.edu.ifce.maracanau.carekobooks.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "forum Strin_reply")
public class ForumReply extends BaseEntity {

    @Column(length = 1000, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "forum_id", nullable = false)
    private Forum forum;

}
