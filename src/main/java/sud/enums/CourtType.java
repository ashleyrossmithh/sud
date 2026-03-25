package sud.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum CourtType implements SelectItem<Integer>{
    RAYON(1, "Районный суд"),
    MIR(2, "Мировой суд"),
    ARBITR(3, "Арбитражный суд"),
    KONST(4, "Конституционный суд");
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
