package br.com.edu.ifce.maracanau.carekobooks.dto.book;

import br.com.edu.ifce.maracanau.carekobooks.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.util.page.AbstractApplicationPagedQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Schema(name = "BookSearch")
public class BookSearchDTO extends AbstractApplicationPagedQuery<Book> {

    private String title;
    private String author;
    private String publisher;
    private LocalDate publishedAt;
    private Integer totalPages;

    @Override
    public Specification<Book> getSpecification() {
        return getEmptySpecification();
    }

    @Override
    public Sort getSort() {
        var fields = List.of("title", "author", "publishedAt", "totalPages");
        return getSort(fields);
    }

}
