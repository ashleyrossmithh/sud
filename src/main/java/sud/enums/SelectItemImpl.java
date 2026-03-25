package sud.enums;

import lombok.Data;

@Data
public class SelectItemImpl<T> implements SelectItem<T> {
    private T code;

    private String label;

    public SelectItemImpl(SelectItem<T> item) {
        this.code = item.getCode();
        this.label = item.getLabel();
    }
}
