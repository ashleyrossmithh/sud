package sud.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


@Getter// такой же етгь есть в ui
public enum DirectionType {
    BOSS(0, "Руководитель", true),
    ZAYAV(1, "Заявитель"),
    OTV(2, "Ответчик"),
    ADVOCATE(3, "Адвокат", true),
    COURT(4, "Судья"),
    HELPER(5, "Юрист", true),
    ZAYAV_ADD(6, "Заинтересованное лицо"),
    OTV_ADD(7, "Заинтересованное лицо"),
    ;

    DirectionType(Integer id, String name, boolean advocateGroup) {
        this.id = id;
        this.name = name;
        this.advocateGroup = advocateGroup;
    }
    DirectionType(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.advocateGroup = false;
    }

    private final Integer id;
    private final String name;
    private boolean advocateGroup = false;

    public static DirectionType parse(Integer id) {
        return Arrays.stream(values()).filter(cur -> cur.id.equals(id)).findFirst().orElse(null);
    }

    public static List<DirectionType> getAdvocatesGroup() {
        return Arrays.stream(DirectionType.values()).filter(DirectionType::isAdvocateGroup).toList();
    }
}
