package sud.core.bc.filter.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import sud.core.bc.filter.CompareTypeEnum;
import jakarta.persistence.criteria.From;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public enum FilterOperationEnum {
    FO_EQUAL("=") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            if (criteria.isInverseOperation()) {
                criteria.setInverseOperation(false);
                return FO_NOT_EQUAL.getSpecification(criteria, path);
            } else {
                return criteria.isIgnoreTime() ? SimpleSpecifications.isLocalDatesEqualIgnoreTime(criteria.getAttributeName(), (LocalDateTime) criteria.getValueToCompare(), path, criteria.getAttributeToCompare()) : SimpleSpecifications.isEqual(criteria.getAttributeName(), criteria.getValueToCompare(), path, criteria.getAttributeToCompare(), criteria.isNotIgnoreCase());
            }
        }

        String getSql(FilterCriteria criteria) {
            return FilterOperationEnum.compareWithInversionAndTrunc(criteria, "=", "!=");
        }
    },
    FO_NOT_EQUAL("!=") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            if (criteria.isInverseOperation()) {
                criteria.setInverseOperation(false);
                return FO_EQUAL.getSpecification(criteria, path);
            } else {
                return SimpleSpecifications.isNotEqual(criteria.getAttributeName(), criteria.getValueToCompare(), path, criteria.getAttributeToCompare(), criteria.isNotIgnoreCase());
            }
        }

        String getSql(FilterCriteria criteria) {
            return FilterOperationEnum.compareWithInversionAndTrunc(criteria, "!=", "=");
        }
    },
    FO_IS_NULL("пусто") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.isNotNull(criteria.getAttributeName(), path) : SimpleSpecifications.isNull(criteria.getAttributeName(), path);
        }

        String getSql(FilterCriteria criteria) {
            String var10000 = criteria.getAttributeName();
            return var10000 + (criteria.isInverseOperation() ? " IS NOT NULL" : " IS NULL");
        }
    },
    FO_IS_NOT_NULL("не пусто") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.isNull(criteria.getAttributeName(), path) : SimpleSpecifications.isNotNull(criteria.getAttributeName(), path);
        }

        String getSql(FilterCriteria criteria) {
            String var10000 = criteria.getAttributeName();
            return var10000 + (criteria.isInverseOperation() ? " IS NULL" : " IS NOT NULL");
        }
    },
    FO_LESS("<") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.isGreaterThanOrEqual(criteria.getAttributeName(), criteria.getValueToCompare(), path, criteria.getAttributeToCompare()) : SimpleSpecifications.isLessThan(criteria.getAttributeName(), criteria.getValueToCompare(), path, criteria.getAttributeToCompare());
        }

        String getSql(FilterCriteria criteria) {
            return FilterOperationEnum.compareWithInversionAndTrunc(criteria, "<", ">=");
        }
    },
    FO_LESS_EQ("<=") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.isGreaterThan(criteria.getAttributeName(), criteria.getValueToCompare(), path, criteria.getAttributeToCompare()) : SimpleSpecifications.isLessThanOrEqual(criteria.getAttributeName(), criteria.getValueToCompare(), path, criteria.getAttributeToCompare());
        }

        String getSql(FilterCriteria criteria) {
            return FilterOperationEnum.compareWithInversionAndTrunc(criteria, "<=", ">");
        }
    },
    FO_GREATER(">") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.isLessThanOrEqual(criteria.getAttributeName(), criteria.getValueToCompare(), path, criteria.getAttributeToCompare()) : SimpleSpecifications.isGreaterThan(criteria.getAttributeName(), criteria.getValueToCompare(), path, criteria.getAttributeToCompare());
        }

        String getSql(FilterCriteria criteria) {
            return FilterOperationEnum.compareWithInversionAndTrunc(criteria, ">", "<=");
        }
    },
    FO_GREATER_EQ(">=") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.isLessThan(criteria.getAttributeName(), criteria.getValueToCompare(), path, criteria.getAttributeToCompare()) : SimpleSpecifications.isGreaterThanOrEqual(criteria.getAttributeName(), criteria.getValueToCompare(), path, criteria.getAttributeToCompare());
        }

        String getSql(FilterCriteria criteria) {
            return FilterOperationEnum.compareWithInversionAndTrunc(criteria, ">=", "<");
        }
    },
    FO_LIKE("содержит") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.notContains(criteria.getAttributeName(), (String) criteria.getValueToCompare(), path, criteria.getAttributeToCompare(), criteria.isNotIgnoreCase()) : SimpleSpecifications.containsLike(criteria.getAttributeName(), (String) criteria.getValueToCompare(), path, criteria.getAttributeToCompare(), criteria.isNotIgnoreCase());
        }

        String getSql(FilterCriteria criteria) {
            if (criteria.getValTypeCode() != FilterValTypeEnum.FVTE_STRING.getCode()) {
                throw new RuntimeException("Операция 'содержит' применима только к строкам");
            } else {
                String var10000 = FilterOperationEnum.applyCaseFlag(criteria.getAttributeName(), criteria.isNotIgnoreCase());
                return var10000 + (criteria.isInverseOperation() ? " NOT LIKE " : " LIKE ") + "'%'||" + FilterOperationEnum.applyCaseFlag(criteria.getCompareAttributeOrBindVar(), criteria.isNotIgnoreCase()) + " || '%'";
            }
        }
    },
    FO_NOT_LIKE("не содержит") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.containsLike(criteria.getAttributeName(), (String) criteria.getValueToCompare(), path, criteria.getAttributeToCompare(), criteria.isNotIgnoreCase()) : SimpleSpecifications.notContains(criteria.getAttributeName(), (String) criteria.getValueToCompare(), path, criteria.getAttributeToCompare(), criteria.isNotIgnoreCase());
        }

        String getSql(FilterCriteria criteria) {
            if (criteria.getValTypeCode() != FilterValTypeEnum.FVTE_STRING.getCode()) {
                throw new RuntimeException("Операция 'не содержит' применима только к строкам");
            } else {
                String var10000 = FilterOperationEnum.applyCaseFlag(criteria.getAttributeName(), criteria.isNotIgnoreCase());
                return var10000 + (criteria.isInverseOperation() ? " LIKE " : " NOT LIKE ") + "'%'||" + FilterOperationEnum.applyCaseFlag(criteria.getCompareAttributeOrBindVar(), criteria.isNotIgnoreCase()) + " || '%'";
            }
        }
    },
    FO_BEGINS_WITH("начинается с") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.notBeginsWith(criteria.getAttributeName(), (String) criteria.getValueToCompare(), path, criteria.getAttributeToCompare(), criteria.isNotIgnoreCase()) : SimpleSpecifications.beginsWith(criteria.getAttributeName(), (String) criteria.getValueToCompare(), path, criteria.getAttributeToCompare(), criteria.isNotIgnoreCase());
        }

        String getSql(FilterCriteria criteria) {
            if (criteria.getValTypeCode() != FilterValTypeEnum.FVTE_STRING.getCode()) {
                throw new RuntimeException("Операция 'начинается с' применима только к строкам");
            } else {
                String var10000 = FilterOperationEnum.applyCaseFlag(criteria.getAttributeName(), criteria.isNotIgnoreCase());
                return var10000 + (criteria.isInverseOperation() ? " NOT" : "") + " LIKE " + FilterOperationEnum.applyCaseFlag(criteria.getCompareAttributeOrBindVar(), criteria.isNotIgnoreCase()) + " || '%'";
            }
        }
    },
    FO_ENDS_WITH("заканчивается на") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.notEndsWith(criteria.getAttributeName(), (String) criteria.getValueToCompare(), path, criteria.getAttributeToCompare(), criteria.isNotIgnoreCase()) : SimpleSpecifications.endsWith(criteria.getAttributeName(), (String) criteria.getValueToCompare(), path, criteria.getAttributeToCompare(), criteria.isNotIgnoreCase());
        }

        String getSql(FilterCriteria criteria) {
            if (criteria.getValTypeCode() != FilterValTypeEnum.FVTE_STRING.getCode()) {
                throw new RuntimeException("Операция 'заканчивается на' применима только к строкам");
            } else {
                String var10000 = FilterOperationEnum.applyCaseFlag(criteria.getAttributeName(), criteria.isNotIgnoreCase());
                return var10000 + (criteria.isInverseOperation() ? " NOT" : "") + " LIKE '%' || " + FilterOperationEnum.applyCaseFlag(criteria.getCompareAttributeOrBindVar(), criteria.isNotIgnoreCase());
            }
        }
    },
    FO_LENGTH_EQUALS("длина =") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.lengthNotEquals(criteria.getAttributeName(), ((BigDecimal) criteria.getValueToCompare()).intValue(), path, criteria.getAttributeToCompare()) : SimpleSpecifications.lengthEquals(criteria.getAttributeName(), criteria.getValueToCompare() == null ? null : ((BigDecimal) criteria.getValueToCompare()).intValue(), path, criteria.getAttributeToCompare());
        }

        String getSql(FilterCriteria criteria) {
            if (criteria.getValTypeCode() != FilterValTypeEnum.FVTE_STRING.getCode()) {
                throw new RuntimeException("Операция 'длина =' применима только к строкам");
            } else {
                String var10000 = criteria.getAttributeName();
                return " LENGTH(" + var10000 + ")" + (criteria.isInverseOperation() ? " != " : "=") + (CompareTypeEnum.ATTRIBUTE.getCode() == criteria.getCompareType() ? " LENGTH(" + criteria.getAttributeToCompare() + ")" : criteria.getBindVar());
            }
        }
    },
    FO_LENGTH_GREATER("длина >") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.lengthLessOrEquals(criteria.getAttributeName(), ((BigDecimal) criteria.getValueToCompare()).intValue(), path, criteria.getAttributeToCompare()) : SimpleSpecifications.lengthGreater(criteria.getAttributeName(), criteria.getValueToCompare() == null ? null : ((BigDecimal) criteria.getValueToCompare()).intValue(), path, criteria.getAttributeToCompare());
        }

        String getSql(FilterCriteria criteria) {
            if (criteria.getValTypeCode() != FilterValTypeEnum.FVTE_STRING.getCode()) {
                throw new RuntimeException("Операция 'длина >' применима только к строкам");
            } else {
                String var10000 = criteria.getAttributeName();
                return " LENGTH(" + var10000 + ")" + (criteria.isInverseOperation() ? " <= " : ">") + (CompareTypeEnum.ATTRIBUTE.getCode() == criteria.getCompareType() ? " LENGTH(" + criteria.getAttributeToCompare() + ")" : criteria.getBindVar());
            }
        }
    },
    FO_LENGTH_LESS("длина <") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isInverseOperation() ? SimpleSpecifications.lengthGreaterOrEquals(criteria.getAttributeName(), ((BigDecimal) criteria.getValueToCompare()).intValue(), path, criteria.getAttributeToCompare()) : SimpleSpecifications.lengthLess(criteria.getAttributeName(), criteria.getValueToCompare() == null ? null : ((BigDecimal) criteria.getValueToCompare()).intValue(), path, criteria.getAttributeToCompare());
        }

        String getSql(FilterCriteria criteria) {
            if (criteria.getValTypeCode() != FilterValTypeEnum.FVTE_STRING.getCode()) {
                throw new RuntimeException("Операция 'длина <' применима только к строкам");
            } else {
                String var10000 = criteria.getAttributeName();
                return " LENGTH(" + var10000 + ")" + (criteria.isInverseOperation() ? " >= " : "<") + (CompareTypeEnum.ATTRIBUTE.getCode() == criteria.getCompareType() ? " LENGTH(" + criteria.getAttributeToCompare() + ")" : criteria.getBindVar());
            }
        }
    },
    FO_IN_LIST("из списка") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return SimpleSpecifications.isInList(criteria.getAttributeName(), (List) criteria.getValueToCompare(), path);
        }

        String getSql(FilterCriteria criteria) {
            List<?> value = (List) criteria.getValueToCompare();
            if (value != null) {
                value = value.stream().map((v) -> {
                    return FilterOperationEnum.stringifyValue(v, criteria.getFilterValTypeEnum());
                }).toList();
            }

            String stringValue = value != null ? value.toString() : "[]";
            String var10000 = criteria.getAttributeName();
            return var10000 + (criteria.isInverseOperation() ? " NOT" : "") + " IN (" + stringValue.substring(1, stringValue.length() - 1) + ")";
        }
    },
    FO_SQL("sql") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return null;
        }

        String getSql(FilterCriteria criteria) {
            return criteria.getSqlText();
        }
    },
  /*  FO_FROM_FILE("из колонки файла") {
        <V> Specification<V> getSpecification(FilterCriteria criteria, From path) {
            return criteria.isNumeric() ? SimpleSpecifications.isInSessionDataNumbers(criteria.getAttributeName(), criteria.getSessionDataBlockId(), path) : SimpleSpecifications.isInSessionDataStrs(criteria.getAttributeName(), criteria.getSessionDataBlockId(), path);
        }

        String getSql(FilterCriteria criteria) {
            return criteria.isNumeric() ? criteria.getAttributeName() + " IN (SELECT KEY1 FROM SESSION_DATA WHERE SESSION_DATA_BLOCK_ID=" + criteria.getSessionDataBlockId() + ")" : criteria.getAttributeName() + " IN (SELECT STR FROM SESSION_DATA WHERE SESSION_DATA_BLOCK_ID=" + criteria.getSessionDataBlockId() + ")";
        }
    }*/;

    public static final String LENGTH = " LENGTH(";
    public static final String OR_SQL = " || '%'";
    public static final String LIKE = " LIKE ";
    private final String operation;

    private FilterOperationEnum(String operation) {
        this.operation = operation;
    }

    public static boolean needSyntheticBindVar(FilterOperationEnum operation) {
        return operation != FO_IN_LIST && operation != FO_IS_NULL && operation != FO_IS_NOT_NULL && operation != FO_SQL;
    }

    private static String withQuotes(Object value) {
        return value == null ? null : "'" + value + "'";
    }

    private static String withToDate(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof LocalDateTime) {
            LocalDateTime ldtv = (LocalDateTime) value;
            return "TO_DATE('" + ldtv.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")) + "', 'dd.mm.yyyy hh24:mi:ss')";
        } else {
            return "TO_DATE('" + ((LocalDate) value).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "', 'dd.mm.yyyy')";
        }
    }

    private static String applyCaseFlag(String attributeName, boolean notIgnoreCase) {
        return notIgnoreCase ? attributeName : " UPPER(" + attributeName + ")";
    }

    private static String stringifyValue(Object value, FilterValTypeEnum filterValTypeEnum) {
        if (value == null) {
            return null;
        } else {
            String var10000;
            switch (filterValTypeEnum) {
                case FVTE_STRING:
                    var10000 = withQuotes(value);
                    break;
                case FVTE_DATE:
                    var10000 = withToDate(value);
                    break;
                case FVTE_DATE_TIME:
                    var10000 = withToDate(value);
                    break;
                default:
                    var10000 = value.toString();
            }

            return var10000;
        }
    }

    private static String compareWithInversionAndTrunc(FilterCriteria criteria, String operation, String inverseOperation) {
        StringBuilder sb = new StringBuilder();
        if (criteria.isIgnoreTime()) {
            if (!criteria.isDate()) {
                throw new RuntimeException("Атрибут ignoreTime возможен только для дат.");
            }

            sb.append("TRUNC(");
        }

        sb.append(criteria.getFilterValTypeEnum().equals(FilterValTypeEnum.FVTE_STRING) ? applyCaseFlag(criteria.getAttributeName(), criteria.isNotIgnoreCase()) : criteria.getAttributeName());
        if (criteria.isIgnoreTime()) {
            sb.append("::date");
        }

        if (criteria.isIgnoreTime()) {
            sb.append(")");
        }

        sb.append(criteria.isInverseOperation() ? inverseOperation : operation);
        if (criteria.isIgnoreTime()) {
            sb.append("TRUNC(");
        }

        sb.append(criteria.getFilterValTypeEnum().equals(FilterValTypeEnum.FVTE_STRING) ? applyCaseFlag(criteria.getCompareAttributeOrBindVar(), criteria.isNotIgnoreCase()) : criteria.getCompareAttributeOrBindVar());
        if (criteria.isIgnoreTime()) {
            sb.append("::date");
        }

        if (criteria.isIgnoreTime()) {
            sb.append(")");
        }

        return sb.toString();
    }

    @JsonCreator
    public static FilterOperationEnum findFilterOperation(String fOperation) {
        FilterOperationEnum[] var1 = values();
        int var2 = var1.length;

        for (int var3 = 0; var3 < var2; ++var3) {
            FilterOperationEnum operation = var1[var3];
            if (operation.getOperation().equals(fOperation)) {
                return operation;
            }
        }

        return null;
    }

    @JsonValue
    public String getOperation() {
        return this.operation;
    }

    abstract <V> Specification<V> getSpecification(FilterCriteria var1, From var2);

    abstract String getSql(FilterCriteria var1);
}
