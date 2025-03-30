package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.exception.BadRequestException;
import br.com.edu.ifce.maracanau.carekobooks.exception.NotFoundException;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto.BookGenreDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import br.com.edu.ifce.maracanau.carekobooks.shared.layer.application.mapper.BaseUpdateMapper;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BookGenreMapper implements BaseUpdateMapper<BookGenre, BookGenreRequest> {

    @Autowired
    protected BookGenreRepository bookGenreRepository;

    public abstract BookGenre toModel(BookGenreRequest request);
    public abstract BookGenreDTO toDTO(BookGenre bookGenre);

    public List<BookGenre> toModel(List<String> genreNames) {
        var uniqueGenreNames = new HashSet<>(genreNames);
        if (uniqueGenreNames.size() != genreNames.size()) {
            throw new BadRequestException("There list contains duplicate genres");
        }

        var bookGenres = bookGenreRepository.findAllByNameIn(genreNames);
        if (bookGenres.size() != genreNames.size()) {
            throw new NotFoundException("Some genres could not be found");
        }

        return bookGenres;
    }

}
