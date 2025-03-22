package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookActivityDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public interface BookActivityMapper {

    @Mapping(source = "userId", target = "user", qualifiedByName = "toUserModelFromId")
    @Mapping(source = "bookId", target = "book", qualifiedByName = "toBookModelFromId")
    BookActivity toModel(BookProgressRequest request);
    BookActivityDTO toDTO(BookActivity bookActivity);

}
