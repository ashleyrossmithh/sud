package sud.core.dto.support;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class LazyLoadEvent {
    /**
     * First row offset.
     */
    public int first;

    /**
     * Number of rows per page.
     */
    public int rows;

    public String sortField;
    public int sortOrder;

    public SortMeta[] multiSortMeta;


    public Pageable toPageable() {
        if (multiSortMeta != null) {
            List<Sort.Order> list = new ArrayList<>();
            Arrays.asList(multiSortMeta).forEach(sortMeta -> {
                list.add(new Sort.Order(toSortDirection(sortMeta.order), sortMeta.field));
            });
            return PageRequest.of(toPageIndex(), rows, Sort.by(list));
        }
        if (sortField != null) {
            return PageRequest.of(toPageIndex(), rows, toSortDirection(), sortField);
        }
        return PageRequest.of(toPageIndex(), rows);
    }

    /**
     * Zero based page index.
     */
    public int toPageIndex() {
        return (first + rows) / rows - 1;
    }

    public Sort.Direction toSortDirection() {
        return sortOrder == 1 ? Sort.Direction.ASC : Sort.Direction.DESC;
    }

    public Sort.Direction toSortDirection(String order) {
        return order.equals("1") ? Sort.Direction.ASC : Sort.Direction.DESC;
    }

    public boolean hasSort() {
        return sortField != null || (multiSortMeta != null && multiSortMeta.length > 0);
    }
}

