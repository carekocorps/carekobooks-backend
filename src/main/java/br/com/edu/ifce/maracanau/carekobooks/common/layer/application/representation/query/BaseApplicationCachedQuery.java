package br.com.edu.ifce.maracanau.carekobooks.common.layer.application.representation.query;

import br.com.edu.ifce.maracanau.carekobooks.common.layer.infrastructure.model.BaseModel;

public abstract class BaseApplicationCachedQuery<T extends BaseModel> extends BaseApplicationQuery<T> {

    public String getCacheKey() {
        return pageNumber.toString();
    }

    public boolean isDefaultCacheSearch() {
        return isDefaultCacheSearch(DEFAULT_PAGE_SIZE);
    }

    protected boolean isDefaultCacheSearch(int defaultPageSize) {
        return pageNumber >= 0
                && pageNumber <= 2
                && pageSize == defaultPageSize
                && createdBefore == null
                && createdAfter == null
                && isAscendingOrder == DEFAULT_IS_ASCENDING_ORDER
                && orderBy.equals(DEFAULT_ORDER_BY);
    }

}
