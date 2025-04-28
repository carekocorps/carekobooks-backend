package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.annotation.IgnoreBaseModelFields;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.request.BookRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.repository.BookRepository;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.module.image.application.mapper.ImageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = {
                ImageMapper.class,
                BookGenreMapper.class
        }
)
public abstract class BookMapper {

    @Autowired
    protected BookRepository bookRepository;

    @IgnoreBaseModelFields
    @Mapping(target = "userAverageScore", ignore = true)
    @Mapping(target = "reviewAverageScore", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "progresses", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "threads", ignore = true)
    public abstract Book toModel(BookRequest request);
    public abstract BookResponse toResponse(Book book);

    @IgnoreBaseModelFields
    @Mapping(target = "userAverageScore", ignore = true)
    @Mapping(target = "reviewAverageScore", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "progresses", ignore = true)
    @Mapping(target = "activities", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "threads", ignore = true)
    public abstract void updateModel(@MappingTarget Book book, BookRequest request);

    public Book toModel(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

}
