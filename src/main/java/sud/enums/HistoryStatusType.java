package sud.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Getter// такой же етгь есть в ui
public enum HistoryStatusType implements SelectItem<Integer> {
    UNKNOWN(0, "Неизвестен"), //такого быть не должно
    CREATED(1, "Создано"), // создано судебное заявление
    PREPARED(2, "Документы подготовлены"),
    SENDED(3, "Документы переданы в суд"),
    REGISTRED(4, "Зарегистрировано в суде"), // присвоен регистрационный номер
    STARTED(5, "Начало судебного процесса"), // Назначен судья
    WAIT(6, "Назначено судебное заседание"),
    WAIT_REPEAT(7, "Назначено повторное судебное заседание"),
    PREPARE_PROCESS(8,"Подготовка к судебному заседанию"),
    PAUSED(9, "Приостановлено судом"), // например запросили доп. документы
    CANCELED(10, "Закрыто"),
    FINISHED(11, "Завершено"),
    ;
    private final Integer id;
    private final String name;

    public static HistoryStatusType parseByCode(Integer code) {
        return Arrays.stream(values()).filter(cur -> cur.id.equals(code)).findFirst().orElse(UNKNOWN);
    }

    @Override
    public Integer getCode() {
        return getId();
    }

    @Override
    public String getLabel() {
        return getName();
    }

    public static List<SelectItemImpl<Integer>> asSelectItems(Integer curStatusCode) {
        HistoryStatusType curStatus = HistoryStatusType.parseByCode(curStatusCode);
        return Arrays.stream(values()).filter(cur -> {
            if (curStatus.ordinal() <= HistoryStatusType.WAIT_REPEAT.ordinal()
                    && cur.ordinal() < curStatus.ordinal()) return false;
            if (curStatus.ordinal() < HistoryStatusType.WAIT.ordinal()
                    && (cur.ordinal() ==  HistoryStatusType.WAIT_REPEAT.ordinal())) return false; // WAIT_REPEAT отображаем только после статауса WAIT
            // curStatus >= HistoryStatusType.WAIT_REPEAT
            if (cur == curStatus) {
                return curStatus == HistoryStatusType.WAIT_REPEAT;
            }
            return true;
        }).map(SelectItemImpl::new).toList();
    }
}
