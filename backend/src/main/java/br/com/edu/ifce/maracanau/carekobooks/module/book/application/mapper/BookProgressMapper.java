package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookProgressDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.shared.application.mapper.BaseMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public abstract class BookProgressMapper implements BaseMapper<BookProgress, BookProgressRequest> {

    @Mapping(source = "userId", target = "user", qualifiedByName = "toUserModelFromId")
    @Mapping(source = "bookId", target = "book", qualifiedByName = "toBookModelFromId")
    public abstract BookProgress toModel(BookProgressRequest request);
    public abstract BookProgressDTO toDTO(BookProgress bookProgress);

}