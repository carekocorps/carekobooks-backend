package br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.common.factory.module.user.application.payload.response.simplified.SimplifiedUserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookActivityResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookActivity;

public class BookActivityResponseFactory {

    private BookActivityResponseFactory() {
    }

    public static BookActivityResponse validResponse(BookActivity activity) {
        var response = new BookActivityResponse();
        response.setId(activity.getId());
        response.setStatus(activity.getStatus());
        response.setPageCount(activity.getPageCount());
        response.setUser(SimplifiedUserResponseFactory.validResponse(activity.getUser()));
        response.setBook(SimplifiedBookResponseFactory.validResponse(activity.getBook()));
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }

}
