package sud.core.dto.support;


import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {

    public final int totalPages;
    public final long totalElements;
    public final List<T> content;
    public long sqlTime = -1;


    public PageResponse(int totalPages, long totalElements, List<T> content) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.content = content;
    }

    public PageResponse(int totalPages, long totalElements, List<T> content, long sqlTime) {
        this(totalPages, totalElements, content);
        this.sqlTime = sqlTime;

    }
}
