package br.com.edu.ifce.maracanau.carekobooks.shared.module.application.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Schema(name = "Page")
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
