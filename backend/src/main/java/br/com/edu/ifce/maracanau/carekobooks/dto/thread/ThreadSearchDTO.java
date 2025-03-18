package br.com.edu.ifce.maracanau.carekobooks.dto.thread;

import br.com.edu.ifce.maracanau.carekobooks.util.page.AbstractApplicationPagedQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Getter
@Setter
@Schema(name = "ThreadSearch")
public class ThreadSearchDTO extends AbstractApplicationPagedQuery<Thread> {

    private String title;

    @Override
    public Specification<Thread> getSpecification() {
        return getEmptySpecification();
    }

    @Override
    public Sort getSort() {
        return null;
    }

}
