package sud.core.bc.filter.api;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ChainOperationEnum {
    CO_AND("&", " AND "),
    CO_OR("||", " OR ");

    private final String chainOperation;
    private final String sqlName;

    private ChainOperationEnum(String chainOperation, String sqlName) {
        this.chainOperation = chainOperation;
        this.sqlName = sqlName;
    }

    @JsonCreator
    public static ChainOperationEnum getChainOperation(String chainOperation) {
        ChainOperationEnum[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ChainOperationEnum operationEnum = var1[var3];
            if (operationEnum.getChainOperation().equals(chainOperation)) {
                return operationEnum;
            }
        }

        return null;
    }

    @JsonValue
    public String getChainOperation() {
        return this.chainOperation;
    }

    @JsonIgnore
    public String getSqlName() {
        return this.sqlName;
    }
}
