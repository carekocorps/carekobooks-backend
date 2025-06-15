package br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response;

import br.com.edu.ifce.maracanau.carekobooks.factory.module.book.application.payload.response.simplified.SimplifiedBookResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.factory.module.user.payload.response.simplified.SimplifiedUserResponseFactory;
import br.com.edu.ifce.maracanau.carekobooks.module.book.application.payload.response.BookProgressResponse;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.domain.entity.BookProgress;

public class BookProgressResponseFactory {

    private BookProgressResponseFactory() {
    }

    public static BookProgressResponse validResponse(BookProgress progress) {
        var response = new BookProgressResponse();
        response.setId(progress.getId());
        response.setStatus(progress.getStatus());
        response.setIsFavorite(progress.getIsFavorite());
        response.setScore(progress.getScore());
        response.setPageCount(progress.getPageCount());
        response.setUser(SimplifiedUserResponseFactory.validResponse(progress.getUser()));
        response.setBook(SimplifiedBookResponseFactory.validResponse(progress.getBook()));
        response.setCreatedAt(progress.getCreatedAt());
        response.setUpdatedAt(progress.getUpdatedAt());
        return response;
    }

}
