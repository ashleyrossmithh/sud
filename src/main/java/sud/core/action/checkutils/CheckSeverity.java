package sud.core.action.checkutils;

import lombok.Getter;

import java.io.Serializable;

public enum CheckSeverity implements
//        ISelectItem<Number>,
        Serializable {
    DATA(-1, "Данные"),
    INFO(0, "Информация"),
    WARNING(1, "Предупреждение"),
    ERROR(2, "Ошибка"),
    CRITICAL_ERROR(3, "Критическая ошибка");

    @Getter
    private final Number code;
    private final String label;

    CheckSeverity(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getDescription() {
        return label;
    }


    public Number getValue() {
        return code;
    }


}

