package br.com.edu.ifce.maracanau.carekobooks.module.book.application.service;

import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookProgressDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper.BookProgressMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.query.BookProgressSearchQuery;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.service.validator.BookProgressValidator;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookProgressRepository;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.page.ApplicationPage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookProgressService {

    private final BookProgressRepository bookProgressRepository;
    private final BookProgressMapper bookProgressMapper;
    private final BookProgressValidator bookProgressValidator;

    public ApplicationPage<BookProgressDTO> search(BookProgressSearchQuery query) {
        var specification = query.getSpecification();
        var sort = query.getSort();
        var pageRequest = PageRequest.of(query.getPageNumber(), query.getPageSize(), sort);
        return new ApplicationPage<>(bookProgressRepository.findAll(specification, pageRequest).map(bookProgressMapper::toDTO));
    }

    public Optional<BookProgressDTO> findById(Long id) {
        return bookProgressRepository.findById(id).map(bookProgressMapper::toDTO);
    }

    public BookProgressDTO create(BookProgressRequest request) {
        var book = bookProgressMapper.toModel(request);
        bookProgressValidator.validate(book);
        return bookProgressMapper.toDTO(bookProgressRepository.save(book));
    }

    public void deleteById(Long id) {
        if (!bookProgressRepository.existsById(id)) {
            throw new NotFoundException("Book not found");
        }

        bookProgressRepository.deleteById(id);
    }

}
