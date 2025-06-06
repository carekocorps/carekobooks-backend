package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseEntityFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookGenreResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookGenreRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookGenre;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookGenreRepository;
import lombok.Setter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BookGenreMapper {

    @Setter(onMethod_ = @Autowired)
    protected BookGenreRepository bookGenreRepository;

    @IgnoreBaseEntityFields
    @Mapping(target = "books", ignore = true)
    public abstract BookGenre toEntity(BookGenreRequest request);

    @Mapping(target = "books", ignore = true)
    public abstract BookGenre toEntity(BookGenreResponse response);
    public abstract BookGenreResponse toResponse(BookGenre genre);

    @IgnoreBaseEntityFields
    @Mapping(target = "books", ignore = true)
    public abstract void updateEntity(@MappingTarget BookGenre genre, BookGenreRequest request);

    public List<BookGenre> toEntity(List<String> genreNames) {
        return bookGenreRepository.findAllByNameIn(genreNames);
    }

}
