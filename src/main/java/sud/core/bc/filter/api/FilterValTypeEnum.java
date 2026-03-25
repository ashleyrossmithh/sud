package sud.core.bc.filter.api;

public enum FilterValTypeEnum {
    FVTE_BOOLEAN(2, "логический"),
    FVTE_DATE(3, "дата"),
    FVTE_DATE_TIME(13, "дата/время"),
    FVTE_STRING(1, "строка"),
    FVTE_NUMBER(6, "число");

    private int code;
    private String label;

    private FilterValTypeEnum(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public static FilterValTypeEnum getByCode(int code) {
        FilterValTypeEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            FilterValTypeEnum fvte = var1[var3];
            if (fvte.getCode() == code) {
                return fvte;
            }
        }

        throw new RuntimeException("Не найден тип атрибута по коду " + code);
    }

    public int getCode() {
        return this.code;
    }

    public String getLabel() {
        return this.label;
    }
}
