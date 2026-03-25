package sud.core.dto.support;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import sud.excel.ExportColumnSetting;

import java.util.List;

@Getter
@Setter
public class PageRequestByFilter<DTO> {
    public DTO example;
    public LazyLoadEvent lazyLoadEvent;
    public Number subTreeNode;
    public List<QueryParam> queryParams;
    private boolean withParents = false;
    private String criteriaName;
    private List<ExportColumnSetting> exportColumnSettings;

    public static PageRequestByFilter<?> simple() {
        PageRequestByFilter<?> pageRequestByFilter = new PageRequestByFilter<>();
        LazyLoadEvent lazyLoadEvent = new LazyLoadEvent();
        lazyLoadEvent.setFirst(0);
        lazyLoadEvent.setRows(10000);
        pageRequestByFilter.setLazyLoadEvent(lazyLoadEvent);
        return pageRequestByFilter;
    }

    public static PageRequestByFilter<?> simple(int numRows) {
        PageRequestByFilter<?> pageRequestByFilter = new PageRequestByFilter<>();
        LazyLoadEvent lazyLoadEvent = new LazyLoadEvent();
        lazyLoadEvent.setFirst(0);
        lazyLoadEvent.setRows(numRows);
        pageRequestByFilter.setLazyLoadEvent(lazyLoadEvent);
        return pageRequestByFilter;
    }


    public static <D> PageRequestByFilter<D> simpleWithExample(int numRows, D example) {
        PageRequestByFilter<D> pageRequestByFilter = new PageRequestByFilter<>();
        pageRequestByFilter.setExample(example);
        LazyLoadEvent lazyLoadEvent = new LazyLoadEvent();
        lazyLoadEvent.setFirst(0);
        lazyLoadEvent.setRows(numRows);
        pageRequestByFilter.setLazyLoadEvent(lazyLoadEvent);
        return pageRequestByFilter;
    }

    public Pageable toPageable() {
        return lazyLoadEvent != null ? lazyLoadEvent.toPageable() : null;
    }


}
