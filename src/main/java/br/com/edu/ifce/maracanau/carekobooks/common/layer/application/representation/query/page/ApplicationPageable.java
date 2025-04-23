package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query.page;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApplicationPageable {

    @Min(0)
    private Integer pageNumber;

    @Min(0)
    @Max(100)
    private Integer pageSize;

    @Min(0)
    private Long totalElements;

}
