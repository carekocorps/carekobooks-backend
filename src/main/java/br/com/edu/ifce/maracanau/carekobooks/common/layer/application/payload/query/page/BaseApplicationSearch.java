package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseApplicationSearch {

    @Min(0)
    @Schema(defaultValue = "0")
    protected Integer pageNumber = 0;

    @Min(0)
    @Max(50)
    @Schema(defaultValue = "10")
    protected Integer pageSize = 10;

    @Schema(
            defaultValue = "id",
            allowableValues = {
                    "id",
                    "created-at",
                    "updated-at"
            }
    )
    protected String orderBy = "id";

    @Schema(defaultValue = "true")
    protected Boolean isAscendingOrder = true;

}
