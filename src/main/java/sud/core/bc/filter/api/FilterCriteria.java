package sud.core.bc.filter.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import sud.core.bc.filter.CompareTypeEnum;
import sud.core.util.LocalDateUtils;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class FilterCriteria implements IFilterItem {
    private String attributeName;
    private FilterOperationEnum operation;
    private Object value;
    private ChainOperationEnum chainOperation;
    private int valTypeCode;
    private int compareType;
    private String attributeToCompare;
    @JsonIgnore
    private FilterValTypeEnum filterValTypeEnum;
    private String sqlText;
    private boolean inverseOperation;
    private boolean notIgnoreCase;
    private boolean ignoreTime;
    private List<FilterCriteria> fixedCriteria;
    private Long sessionDataBlockId;
    private boolean calculated;
    private boolean skippedIfNullValue;
    private String bindVar;

    @ConstructorProperties({"attributeName", "operation", "value", "chainOperation", "valTypeCode", "fixedCriteria", "calculated", "skippedIfNullValue", "compareType", "attributeToCompare", "sqlText", "inverseOperation", "notIgnoreCase", "ignoreTime", "sessionDataBlockId"})
    public FilterCriteria(String attributeName, FilterOperationEnum operation, Object value, ChainOperationEnum chainOperation, int valTypeCode, List<FilterCriteria> fixedCriteria, boolean calculated, boolean skippedIfNullValue, int compareType, String attributeToCompare, String sqlText, boolean inverseOperation, boolean notIgnoreCase, boolean ignoreTime, Long sessionDataBlockId) {
        this.operation = FilterOperationEnum.FO_EQUAL;
        this.chainOperation = ChainOperationEnum.CO_AND;
        this.valTypeCode = 6;
        this.compareType = 1;
        this.filterValTypeEnum = FilterValTypeEnum.FVTE_NUMBER;
        this.calculated = false;
        this.skippedIfNullValue = false;
        this.attributeName = attributeName;
        this.operation = operation;
        this.value = value;
        this.chainOperation = chainOperation;
        this.setValTypeCode(valTypeCode);
        this.fixedCriteria = fixedCriteria;
        this.calculated = calculated;
        this.skippedIfNullValue = skippedIfNullValue;
        this.compareType = compareType;
        this.attributeToCompare = attributeToCompare;
        this.sqlText = sqlText;
        this.inverseOperation = inverseOperation;
        this.notIgnoreCase = notIgnoreCase;
        this.ignoreTime = ignoreTime;
        this.sessionDataBlockId = sessionDataBlockId;
    }

    private FilterCriteria(String attributeName, FilterOperationEnum operation) {
        this.operation = FilterOperationEnum.FO_EQUAL;
        this.chainOperation = ChainOperationEnum.CO_AND;
        this.valTypeCode = 6;
        this.compareType = 1;
        this.filterValTypeEnum = FilterValTypeEnum.FVTE_NUMBER;
        this.calculated = false;
        this.skippedIfNullValue = false;
        this.attributeName = attributeName;
        this.operation = operation;
    }

    public FilterCriteria() {
        this.operation = FilterOperationEnum.FO_EQUAL;
        this.chainOperation = ChainOperationEnum.CO_AND;
        this.valTypeCode = 6;
        this.compareType = 1;
        this.filterValTypeEnum = FilterValTypeEnum.FVTE_NUMBER;
        this.calculated = false;
        this.skippedIfNullValue = false;
    }

    public FilterCriteria(String attributeName, FilterOperationEnum operation, Object value, ChainOperationEnum chainOperation, int valTypeCode) {
        this(attributeName, operation, value, chainOperation, valTypeCode, (List)null, false, false, 1, (String)null, (String)null, false, false, false, (Long)null);
    }

    public static FilterCriteria of(String attributeName, FilterOperationEnum operation, Object value, int valTypeCode) {
        FilterCriteria filterCriteria = new FilterCriteria(attributeName, operation);
        filterCriteria.value = value;
        filterCriteria.setValTypeCode(valTypeCode);
        return filterCriteria;
    }

    public static FilterCriteria of(String attributeName, FilterOperationEnum operation, String value) {
        FilterCriteria filterCriteria = new FilterCriteria(attributeName, operation);
        filterCriteria.value = value;
        filterCriteria.setFilterValTypeEnum(FilterValTypeEnum.FVTE_STRING);
        return filterCriteria;
    }

    public static FilterCriteria of(String attributeName, FilterOperationEnum operation, Number value) {
        FilterCriteria filterCriteria = new FilterCriteria(attributeName, operation);
        filterCriteria.value = value;
        filterCriteria.setFilterValTypeEnum(FilterValTypeEnum.FVTE_NUMBER);
        return filterCriteria;
    }

    public static FilterCriteria of(String attributeName, FilterOperationEnum operation, LocalDate value) {
        FilterCriteria filterCriteria = new FilterCriteria(attributeName, operation);
        filterCriteria.value = value;
        filterCriteria.setFilterValTypeEnum(FilterValTypeEnum.FVTE_DATE);
        return filterCriteria;
    }

    public static FilterCriteria of(String attributeName, FilterOperationEnum operation, LocalDateTime value) {
        FilterCriteria filterCriteria = new FilterCriteria(attributeName, operation);
        filterCriteria.value = value;
        filterCriteria.setFilterValTypeEnum(FilterValTypeEnum.FVTE_DATE_TIME);
        return filterCriteria;
    }

    public static FilterCriteria of(String attributeName, FilterOperationEnum operation, List<Integer> value) {
        FilterCriteria filterCriteria = new FilterCriteria(attributeName, operation);
        filterCriteria.value = value;
        filterCriteria.setFilterValTypeEnum(FilterValTypeEnum.FVTE_NUMBER);
        return filterCriteria;
    }

    public static FilterCriteria of(String sqlText) {
        FilterCriteria filterCriteria = new FilterCriteria((String)null, FilterOperationEnum.FO_SQL);
        filterCriteria.sqlText = sqlText;
        return filterCriteria;
    }

    public String getSqlText() {
        return this.sqlText;
    }

    public boolean isInverseOperation() {
        return this.inverseOperation;
    }

    public void setInverseOperation(boolean inverseOperation) {
        this.inverseOperation = inverseOperation;
    }

    public boolean isNotIgnoreCase() {
        return this.notIgnoreCase;
    }

    public void setNotIgnoreCase(boolean notIgnoreCase) {
        this.notIgnoreCase = notIgnoreCase;
    }

    public boolean isIgnoreTime() {
        return this.ignoreTime;
    }

    public void setIgnoreTime(boolean ignoreTime) {
        this.ignoreTime = ignoreTime;
    }

    public FilterValTypeEnum getFilterValTypeEnum() {
        return this.filterValTypeEnum;
    }

    private void setFilterValTypeEnum(FilterValTypeEnum filterValTypeEnum) {
        this.filterValTypeEnum = filterValTypeEnum;
        this.valTypeCode = filterValTypeEnum.getCode();
    }

    @JsonIgnore
    public boolean isNumeric() {
        return this.valTypeCode == FilterValTypeEnum.FVTE_NUMBER.getCode();
    }

    @JsonIgnore
    public boolean isDate() {
        return this.valTypeCode == FilterValTypeEnum.FVTE_DATE.getCode() || this.valTypeCode == FilterValTypeEnum.FVTE_DATE_TIME.getCode();
    }

    public boolean isCalculated() {
        return this.calculated;
    }

    public boolean isSkippedIfNullValue() {
        return this.skippedIfNullValue;
    }

    public void setSkippedIfNullValue(boolean skippedIfNullValue) {
        this.skippedIfNullValue = skippedIfNullValue;
    }

    public int getCompareType() {
        return this.compareType;
    }

    public void setCompareType(int compareType) {
        this.compareType = compareType;
    }

    public String getAttributeToCompare() {
        return CompareTypeEnum.ATTRIBUTE.getCode() == this.compareType ? this.attributeToCompare : null;
    }

    public void setAttributeToCompare(String attributeToCompare) {
        this.attributeToCompare = attributeToCompare;
    }

    public void replaceCalculatedValue(Object value) {
        this.value = value;
        this.calculated = false;
        if (value == null) {
            if (this.operation == FilterOperationEnum.FO_EQUAL) {
                this.operation = FilterOperationEnum.FO_IS_NULL;
            } else if (this.operation == FilterOperationEnum.FO_NOT_EQUAL) {
                this.operation = FilterOperationEnum.FO_IS_NOT_NULL;
            }
        }

    }

    public String toString() {
        if (this.attributeName == null && this.sqlText != null) {
            return "SqlText = " + this.sqlText;
        } else {
            StringBuilder sb = new StringBuilder(this.attributeName);
            sb.append(this.operation.getOperation());
            if (this.value != null) {
                sb.append(this.value);
            } else {
                sb.append(this.valTypeCode != 1 ? "null" : "");
            }

            sb.append(this.chainOperation == null ? "null" : this.chainOperation.getChainOperation()).append(this.valTypeCode);
            if (this.fixedCriteria != null) {
                sb.append(this.fixedCriteria);
            }

            return sb.toString();
        }
    }

    public List<FilterCriteria> getFixedCriteria() {
        return this.fixedCriteria;
    }

    public void setFixedCriteria(List<FilterCriteria> fixedCriteria) {
        this.fixedCriteria = fixedCriteria;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public FilterOperationEnum getOperation() {
        return this.operation;
    }

    public int getValTypeCode() {
        return this.valTypeCode;
    }

    private void setValTypeCode(int valTypeCode) {
        this.valTypeCode = valTypeCode;
        this.filterValTypeEnum = FilterValTypeEnum.getByCode(valTypeCode);
    }

    private Object cast(Object value) {
        if ("null".equals(value)) {
            return null;
        } else {
            switch (this.filterValTypeEnum) {
                case FVTE_BOOLEAN:
                    return Boolean.valueOf(value.toString());
                case FVTE_DATE_TIME:
                    LocalDateTime parsed = LocalDateUtils.localDateTimeFromIsoInstant(value.toString());
                    if (this.ignoreTime) {
                        return parsed.withHour(0).withMinute(0).withSecond(0);
                    }

                    return parsed.withNano(0);
                case FVTE_DATE:
                    return LocalDateUtils.localDateFromIsoInstant(value.toString());
                case FVTE_NUMBER:
                    return value != null && !value.toString().isEmpty() ? new BigDecimal(value.toString()) : null;
                default:
                    return value;
            }
        }
    }

    @JsonProperty("value")
    public Object getValueToCompare() {
        return this.value;
    }

    public ChainOperationEnum getChainOperation() {
        return this.chainOperation;
    }

    @JsonIgnore
    public String getBindVar() {
        return this.bindVar;
    }

    public void setBindVar(String bindVar) {
        this.bindVar = bindVar;
    }

    @JsonIgnore
    public String getCompareAttributeOrBindVar() {
        return CompareTypeEnum.ATTRIBUTE.getCode() == this.compareType ? this.getAttributeToCompare() : this.getBindVar();
    }

    public Long getSessionDataBlockId() {
        return this.sessionDataBlockId;
    }

    public void setSessionDataBlockId(Long sessionDataBlockId) {
        this.sessionDataBlockId = sessionDataBlockId;
    }
}
