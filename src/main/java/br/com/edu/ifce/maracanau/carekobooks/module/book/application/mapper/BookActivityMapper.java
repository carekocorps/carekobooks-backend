package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.dto.BookActivityDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public interface BookActivityMapper {

    @Mapping(target = "user", expression = "java(userMapper.toModel(true))")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    BookActivity toModel(BookProgressRequest request);
    BookActivityDTO toDTO(BookActivity bookActivity);

}
