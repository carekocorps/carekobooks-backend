package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.module.user.shared.AuthenticatedUser;
import br.com.edu.ifce.maracanau.carekobooks.shared.exception.ForbiddenException;
import br.com.edu.ifce.maracanau.carekobooks.shared.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookActivityDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookActivityMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.query.BookActivitySearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookActivityRepository;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.ApplicationPage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookActivityService {

    private final BookActivityRepository bookActivityRepository;
    private final BookActivityMapper bookActivityMapper;

    public ApplicationPage<BookActivityDTO> search(BookActivitySearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookActivityRepository.findAll(specification, pageRequest).map(bookActivityMapper::toDTO));
    }

    public Optional<BookActivityDTO> findById(Long id) {
        return bookActivityRepository.findById(id).map(bookActivityMapper::toDTO);
    }

    @Transactional
    public void deleteById(Long id) {
        var bookActivity = bookActivityRepository.findById(id).orElse(null);
        if (bookActivity == null) {
            throw new NotFoundException("Book not found");
        }

        if (!AuthenticatedUser.isAuthorOrAdmin(bookActivity.getUser().getId())) {
            throw new ForbiddenException("Forbidden");
        }

        bookActivityRepository.deleteById(id);
    }

}
