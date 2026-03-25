package sud.core.bc.filter;

public enum CompareTypeEnum {
    FIX_VALUE(1),
    ATTRIBUTE(2);

    private int code;

    CompareTypeEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
