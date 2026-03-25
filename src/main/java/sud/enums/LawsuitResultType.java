package sud.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Getter// такой же етгь есть в ui
public enum LawsuitResultType implements SelectItem<Integer> {
    SUCCESS(0, "Усершно"),
    FAILURE(1, "Неудачно"),
    OK(3, "Частично"),
    ;
    private final Integer id;
    private final String name;

    public static LawsuitResultType parseByCode(Integer code) {
        return Arrays.stream(values()).filter(cur -> cur.id.equals(code)).findFirst().orElse(null);
    }

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
