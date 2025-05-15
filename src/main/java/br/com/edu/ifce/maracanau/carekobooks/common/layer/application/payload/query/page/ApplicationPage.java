package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class ApplicationPage<T> {

    private List<T> content;
    private ApplicationPageable pageable;

    public ApplicationPage(Page<T> page) {
        content = page.getContent();
        pageable = new ApplicationPageable(
                page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(),
                page.getTotalElements()
        );
    }

}
