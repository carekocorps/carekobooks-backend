package br.com.edu.ifce.maracanau.carekobooks.util.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomPageable {

    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;

}
