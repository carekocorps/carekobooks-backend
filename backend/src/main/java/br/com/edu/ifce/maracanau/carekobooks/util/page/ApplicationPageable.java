package br.com.edu.ifce.maracanau.carekobooks.util.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(name = "Pageable")
public class ApplicationPageable {

    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;

}
