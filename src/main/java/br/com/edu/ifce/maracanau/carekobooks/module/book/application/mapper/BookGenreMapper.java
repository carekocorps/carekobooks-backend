package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookGenreResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.BaseUpdateMapper;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring",
        config = BaseUpdateMapper.class
)
public abstract class BookGenreMapper implements BaseUpdateMapper<BookGenre, BookGenreRequest> {

    @Autowired
    protected BookGenreRepository bookGenreRepository;

    public abstract BookGenre toModel(BookGenreRequest request);
    public abstract BookGenre toModel(BookGenreResponse bookGenreResponse);
    public abstract BookGenreResponse toResponse(BookGenre bookGenre);

    public List<BookGenre> toModel(List<String> genreNames) {
        return bookGenreRepository.findAllByNameIn(genreNames);
    }

}
