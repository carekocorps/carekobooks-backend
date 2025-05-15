package br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.domain.entity.BaseModel;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.domain.entity.User;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "book_thread_replies")
public class BookThreadReply extends BaseModel {

    @Column(length = 1000, nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "thread_id", nullable = false)
    private BookThread thread;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private BookThreadReply parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    private List<BookThreadReply> children;

    @Transactional
    public Boolean getIsContainingChildren() {
        return children != null && !children.isEmpty();
    }

}
