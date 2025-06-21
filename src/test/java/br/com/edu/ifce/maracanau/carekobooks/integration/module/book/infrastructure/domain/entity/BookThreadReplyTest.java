package br.com.edu.ifce.maracanau.carekobooks.integration.module.book.infrastructure.domain.entity;

import br.com.edu.ifce.maracanau.carekobooks.common.annotation.IntegrationTest;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.infrastructure.domain.entity.BookThreadReplyFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.infrastructure.domain.entity.UserFactory;
import br.com.edu.ifce.maracanau.carekobooks.integration.common.config.PostgresContainerConfig;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadReplyRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookThreadRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.user.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@IntegrationTest
@Import({PostgresContainerConfig.class})
@DataJpaTest
class BookThreadReplyTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookThreadRepository bookThreadRepository;

    @Autowired
    private BookThreadReplyRepository bookThreadReplyRepository;

    @BeforeEach
    void setUp() {
        tearDown();
    }

    @AfterEach
    void tearDown() {
        bookThreadReplyRepository.deleteAllInBatch();
        bookThreadRepository.deleteAllInBatch();
        bookRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        entityManager.clear();
    }

    @Test
    void getIsContainingChildren_withExistingChildren_shouldReturnTrue() {
        // Arrange
        var user = userRepository.save(UserFactory.validUserWithNullId());
        var book = bookRepository.save(BookFactory.validBookWithNullIdAndEmptyGenres());
        var thread = bookThreadRepository.save(BookThreadFactory.validThreadWithNullId(book, user));
        var parentReply = bookThreadReplyRepository.save(BookThreadReplyFactory.validReplyWithNullId(thread, user));
        bookThreadReplyRepository.save(BookThreadReplyFactory.validReplyWithNullId(parentReply, thread, user));

        entityManager.clear();
        var updatedParentReply = bookThreadReplyRepository.findById(parentReply.getId()).orElseThrow();

        // Act
        var result = updatedParentReply.getIsContainingChildren();

        // Assert
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(bookRepository.count()).isEqualTo(1);
        assertThat(bookThreadRepository.count()).isEqualTo(1);
        assertThat(bookThreadReplyRepository.count()).isEqualTo(2);
        assertThat(result).isTrue();
    }

}
