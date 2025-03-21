package br.com.edu.ifce.maracanau.carekobooks.module.book.application.mapper;

import br.com.edu.ifce.maracanau.carekobooks.module.book.application.dto.BookActivityDTO;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.request.BookProgressRequest;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookActivity;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.user.application.mapper.UserMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, BookMapper.class})
public interface BookActivityMapper {

    BookActivity toModel(BookProgressRequest request);
    BookActivity toModel(BookProgress bookProgress);
    BookActivityDTO toDTO(BookActivity bookActivity);

}
