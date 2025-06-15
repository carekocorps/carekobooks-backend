package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookThreadResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookThread;

public class BookThreadResponseFactory {

    private BookThreadResponseFactory() {
    }

    public static BookThreadResponse validResponse(BookThread thread) {
        var response = new BookThreadResponse();
        response.setId(thread.getId());
        response.setTitle(thread.getTitle());
        response.setDescription(thread.getDescription());
        response.setUser(SimplifiedUserResponseFactory.validResponse(thread.getUser()));
        response.setBook(SimplifiedBookResponseFactory.validResponse(thread.getBook()));
        response.setCreatedAt(thread.getCreatedAt());
        response.setUpdatedAt(thread.getUpdatedAt());
        return response;
    }

}
