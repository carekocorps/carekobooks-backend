package br.com.edu.ifce.maracanau.carekobooks.util.page;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractCustomPagedSearch {

    protected Integer pageNumber = 0;
    protected Integer pageSize = 10;

    protected String orderBy = "id";
    protected Boolean isAscendent = true;

}
