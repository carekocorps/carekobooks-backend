package br.com.edu.ifce.maracanau.carekobooks.util.page;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class CustomPage<T> {

    private List<T> content;
    private CustomPageable pageable;

    public CustomPage(Page<T> page) {
        content = page.getContent();
        pageable = new CustomPageable(
                page.getPageable().getPageNumber(),
                page.getPageable().getPageSize(),
                page.getTotalElements()
        );
    }

}
