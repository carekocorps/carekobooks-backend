package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.payload.query.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseApplicationSearch {

    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_ORDER_BY = "id";
    public static final boolean DEFAULT_IS_ASCENDING_ORDER = true;

    @Schema(defaultValue = "0")
    protected Integer pageNumber = DEFAULT_PAGE_NUMBER;

    @Schema(defaultValue = "10")
    protected Integer pageSize = DEFAULT_PAGE_SIZE;

    @Schema(
            defaultValue = "id",
            allowableValues = {
                    "id",
                    "created-at",
                    "updated-at"
            }
    )
    protected String orderBy = DEFAULT_ORDER_BY;

    @Schema(defaultValue = "true")
    protected Boolean isAscendingOrder = DEFAULT_IS_ASCENDING_ORDER;
}
