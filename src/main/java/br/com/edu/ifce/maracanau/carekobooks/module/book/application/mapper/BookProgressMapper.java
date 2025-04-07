package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.response.BookProgressResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.representation.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserContextMapper;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import br.com.edu.ifce.maracanau.carekobooks.common.layer.application.mapper.BaseUpdateMapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public abstract class BookProgressMapper implements BaseUpdateMapper<BookProgress, BookProgressRequest> {

    @Autowired
    protected UserContextMapper userContextMapper;

    @Mapping(target = "user", expression = "java(userContextMapper.toModel())")
    @Mapping(target = "book", expression = "java(bookMapper.toModel(request.getBookId()))")
    public abstract BookProgress toModel(BookProgressRequest request);
    public abstract BookProgressResponse toResponse(BookProgress bookProgress);

}
