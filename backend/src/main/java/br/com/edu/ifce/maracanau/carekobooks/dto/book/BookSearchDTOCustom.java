package br.com.edu.ifce.maracanau.carekobooks.dto.book;

import br.com.edu.ifce.maracanau.carekobooks.model.Book;
import br.com.edu.ifce.maracanau.carekobooks.util.page.AbstractCustomPagedQuery;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@Getter
@Setter
public class BookSearchDTOCustom extends AbstractCustomPagedQuery<Book> {

    private String title;
    private String synopsis;
    private String author;
    private String publisher;
    private LocalDate publishedAt;
    private Integer totalPages;
    private Integer scoreSum;
    private Integer scoreCount;

    @Override
    public Specification<Book> getSpecification() {
        return null;
    }

    @Override
    public Sort getSort() {
        return null;
    }

}
