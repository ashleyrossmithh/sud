package sud.core.dto.support;


import sud.core.bc.filter.api.FilterExpression;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public interface IBaseDTOService<D> {
    PageResponse<D> flexFilter(PageRequestByFilter<FilterExpression> var1);

    default boolean checkSql(PageRequestByFilter<FilterExpression> filter) {
        return true;
    }

    default List<D> complete(String query, int maxResults, FilterExpression templateExpressions) {
        return this.complete(query, maxResults, templateExpressions, (List)null);
    }

    default List<D> complete(String query, int maxResults, FilterExpression templateExpressions, List<QueryParam> queryParams) {
        return this.complete(query, maxResults, templateExpressions);
    }

    default D createNew() {
        return null;
    }

    default List<D> search(String field, Object value, int maxResults) {
        return Collections.emptyList();
    }

 /*   BigDecimal getSum(SumFieldRequest var1);

    @Transactional(
            readOnly = true
    )
    default SumFieldResponse doFieldSum(SumFieldRequest sumFieldRequest) {
        long startTime = System.currentTimeMillis();
        BigDecimal sum = this.getSum(sumFieldRequest);
        return new SumFieldResponse(sum, System.currentTimeMillis() - startTime);
    }*/
}
