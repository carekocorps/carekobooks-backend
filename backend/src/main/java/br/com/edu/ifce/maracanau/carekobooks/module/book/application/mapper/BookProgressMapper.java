package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto.BookProgressDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.mapper.BaseUpdateMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public interface BookProgressMapper extends BaseUpdateMapper<BookProgress, BookProgressRequest> {

    @Mapping(target = "user", expression = "java(userMapper.toModel())")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    BookProgress toModel(BookProgressRequest request);
    BookProgressDTO toDTO(BookProgress bookProgress);

}
