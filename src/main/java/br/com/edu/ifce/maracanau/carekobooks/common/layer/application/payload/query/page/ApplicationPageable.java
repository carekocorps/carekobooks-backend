package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApplicationPageable {

    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;

}
