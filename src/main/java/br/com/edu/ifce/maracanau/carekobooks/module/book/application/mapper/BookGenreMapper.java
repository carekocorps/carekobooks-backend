package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookGenreResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BookGenreMapper {

    @Autowired
    protected BookGenreRepository bookGenreRepository;

    @IgnoreBaseModelFields
    @Mapping(target = "books", ignore = true)
    public abstract BookGenre toModel(BookGenreRequest request);

    @Mapping(target = "books", ignore = true)
    public abstract BookGenre toModel(BookGenreResponse response);
    public abstract BookGenreResponse toResponse(BookGenre genre);

    @IgnoreBaseModelFields
    @Mapping(target = "books", ignore = true)
    public abstract void updateModel(@MappingTarget BookGenre genre, BookGenreRequest request);

    public List<BookGenre> toModel(List<String> genreNames) {
        return bookGenreRepository.findAllByNameIn(genreNames);
    }

}
