package sud.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum PersonType implements SelectItem<Integer>{
    PERSON(1, "ФЛ"),
    LEGAL(2, "ЮЛ"),
    ;
    private final Integer id;
    private final String name;

    @Override
    public Integer getCode() {
        return getId();
    }

    @Override
    public String getLabel() {
        return getName();
    }

    public static List<SelectItemImpl<Integer>> asSelectItems() {
        return Arrays.stream(values()).map(SelectItemImpl::new).toList();
    }
}