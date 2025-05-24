package br.com.edu.ifce.maracanau.carekobooks.factory.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.factory.book.infrastructure.domain.entity.BookThreadFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.user.payload.response.UserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;

public class BookThreadResponseFactory {

    public static BookThreadResponse validResponse() {
        var thread = BookThreadFactory.validThread();
        var response = new BookThreadResponse();
        response.setId(thread.getId());
        response.setTitle(thread.getTitle());
        response.setDescription(thread.getDescription());
        response.setUser(UserResponseFactory.validResponse());
        response.setCreatedAt(thread.getCreatedAt());
        response.setUpdatedAt(thread.getUpdatedAt());
        return response;
    }

}
