package br.com.edu.ifce.maracanau.carekobooks.module.book.application.query;

import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.BookProgress;
import br.com.edu.ifce.maracanau.carekobooks.module.book.infrastructure.model.enums.BookProgressStatus;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.query.BaseApplicationPageQuery;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.query.annotation.Searchable;
import br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page.query.annotation.Sortable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookProgressSearchQuery extends BaseApplicationPageQuery<BookProgress> {

    @Sortable
    @Searchable
    private BookProgressStatus status;

    @Sortable(name = "favorite")
    @Searchable
    private Boolean isMarkedAsFavorite;

    @Sortable
    @Searchable
    private Integer score;

    @Sortable(name = "pages-read")
    @Searchable
    private Integer pagesRead;

    @Searchable(name = "user.id")
    private Long userId;

    @Searchable(name = "book.id")
    private Long bookId;

    @Override
    @Schema(
            defaultValue = "id",
            allowableValues = {
                    "id",
                    "status",
                    "favorite",
                    "score",
                    "pages-read",
                    "created-at",
                    "updated-at"
            }
    )
    public String getOrderBy() {
        return super.getOrderBy();
    }

}
