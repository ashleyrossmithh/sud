package sud.core.bc.filter.api;


import com.fasterxml.jackson.annotation.JsonIgnore;
import sud.core.bc.filter.CompareTypeEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

public class FilterExpression implements IFilterItem {
    public static final FilterExpression EMPTY = new FilterExpression(FilterCriteria.of(""));
    private FilterCriteria value;
    private ChainOperationEnum chainOperation;
    private List<FilterExpression> children;

    public FilterExpression(FilterCriteria value) {
        this.chainOperation = ChainOperationEnum.CO_AND;
        this.value = value;
    }

    public FilterExpression(FilterCriteria value, ChainOperationEnum chainOperation) {
        this(value);
        this.setChainOperation(chainOperation);
    }

    public FilterExpression(List<FilterExpression> children, ChainOperationEnum chainOperation) {
        this.chainOperation = ChainOperationEnum.CO_AND;
        this.children = children;
        this.setChainOperation(chainOperation);
        this.setValue((FilterCriteria)null);
    }

    public static FilterExpressionBuilder builder() {
        return new FilterExpressionBuilder();
    }

    public static FilterExpression fromSql(String sql) {
        return new FilterExpression(FilterCriteria.of(sql));
    }

    public static List<FilterCriteria> getFlatCriteria(FilterExpression filterExpression) {
        return filterExpression.flattened().map(FilterExpression::getValue).filter((criterion) -> {
            return criterion != null && FilterOperationEnum.needSyntheticBindVar(criterion.getOperation());
        }).toList();
    }

    public static List<String> getFlatFilterAttributes(FilterExpression filterExpression) {
        return filterExpression.flattened().map(FilterExpression::getValue).filter(Objects::nonNull).map(FilterCriteria::getAttributeName).filter(Objects::nonNull).distinct().map(String::toLowerCase).toList();
    }

    public static boolean hasSqlOperation(FilterExpression filterExpression) {
        return filterExpression.flattened().map(FilterExpression::getValue).filter(Objects::nonNull).map(FilterCriteria::getOperation).anyMatch((operation) -> {
            return operation.equals(FilterOperationEnum.FO_SQL);
        });
    }

    public static FilterCriteria getSqlCriteria(FilterExpression filterExpression) {
        return (FilterCriteria)filterExpression.flattened().map(FilterExpression::getValue).filter(Objects::nonNull).filter((filter) -> {
            return filter.getOperation().equals(FilterOperationEnum.FO_SQL);
        }).findAny().orElse(null);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.value == null && (this.children == null || this.children.isEmpty());
    }

    public List<FilterExpression> getChildren() {
        return this.children;
    }

    public FilterCriteria getValue() {
        return this.value;
    }

    public void setValue(FilterCriteria value) {
        this.value = value;
    }

    @JsonIgnore
    public boolean isExpression() {
        return this.children != null && !this.children.isEmpty();
    }

    @JsonIgnore
    public boolean isSingleCriteria() {
        return !this.isExpression();
    }

    @JsonIgnore
    public boolean isPlainList() {
        return this.children != null && this.children.stream().allMatch(FilterExpression::isSingleCriteria);
    }

    public ChainOperationEnum getChainOperation() {
        return this.children != null ? this.chainOperation : this.getValue().getChainOperation();
    }

    public void setChainOperation(ChainOperationEnum operation) {
        this.chainOperation = operation;
    }

    public String toString() {
        if (this.getValue() != null || this.getChildren() != null && !this.getChildren().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            if (this.isSingleCriteria()) {
                sb.append(this.getValue());
            } else if (this.getChildren() != null) {
                this.getChildren().forEach((child) -> {
                    sb.append(child.toString());
                });
            }

            sb.append(")").append(this.getChainOperation() == null ? "null" : this.getChainOperation().getChainOperation());
            return sb.toString();
        } else {
            return "";
        }
    }

    private Stream<FilterExpression> flattened() {
        return this.children == null ? Stream.of(this) : Stream.concat(Stream.of(this), this.children.stream().flatMap(FilterExpression::flattened));
    }

    @JsonIgnore
    public Map<String, Object> getSyntheticParams() {
        List<FilterCriteria> list = getFlatCriteria(this);
        Map<String, Object> result = new HashMap();
        list.forEach((criterion) -> {
            if (criterion.getCompareType() != CompareTypeEnum.ATTRIBUTE.getCode()) {
                result.put(criterion.getBindVar().substring(1), criterion.getValueToCompare());
            }

        });
        return result;
    }

    public FilterExpression() {
        this.chainOperation = ChainOperationEnum.CO_AND;
    }

    public FilterExpression(FilterCriteria value, ChainOperationEnum chainOperation, List<FilterExpression> children) {
        this.chainOperation = ChainOperationEnum.CO_AND;
        this.value = value;
        this.chainOperation = chainOperation;
        this.children = children;
    }

    public static class FilterExpressionBuilder {
        private FilterCriteria value;
        private ChainOperationEnum chainOperation;
        private ArrayList<FilterExpression> children;

        FilterExpressionBuilder() {
            this.chainOperation = ChainOperationEnum.CO_AND;
        }

        public FilterExpressionBuilder value(FilterCriteria value) {
            this.value = value;
            return this;
        }

        public FilterExpressionBuilder chainOperation(ChainOperationEnum chainOperation) {
            this.chainOperation = chainOperation;
            return this;
        }

        public FilterExpressionBuilder and() {
            this.chainOperation = ChainOperationEnum.CO_AND;
            return this;
        }

        public FilterExpressionBuilder or() {
            this.chainOperation = ChainOperationEnum.CO_OR;
            return this;
        }

        public FilterExpressionBuilder child(FilterExpression child) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(child);
            return this;
        }

        public FilterExpressionBuilder criteria(FilterCriteria filterCriteria) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(filterCriteria));
            return this;
        }

        public FilterExpressionBuilder criteria(String attributeName, FilterOperationEnum operation, Object value, int valTypeCode) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(FilterCriteria.of(attributeName, operation, value, valTypeCode)));
            return this;
        }

        public FilterExpressionBuilder criteria(String attributeName, FilterOperationEnum operation, String value) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(FilterCriteria.of(attributeName, operation, value)));
            return this;
        }

        public FilterExpressionBuilder criteria(String attributeName, FilterOperationEnum operation, Number value) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(FilterCriteria.of(attributeName, operation, value)));
            return this;
        }

        public FilterExpressionBuilder criteria(String attributeName, FilterOperationEnum operation, LocalDate value) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(FilterCriteria.of(attributeName, operation, value)));
            return this;
        }

        public FilterExpressionBuilder criteria(String attributeName, FilterOperationEnum operation, LocalDateTime value) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(FilterCriteria.of(attributeName, operation, value)));
            return this;
        }

        public FilterExpressionBuilder criteria(String attributeName, FilterOperationEnum operation, List<Integer> value) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(FilterCriteria.of(attributeName, operation, value)));
            return this;
        }

        public FilterExpressionBuilder equalValue(String attributeName, String value) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(FilterCriteria.of(attributeName, FilterOperationEnum.FO_EQUAL, value)));
            return this;
        }

        public FilterExpressionBuilder equalValue(String attributeName, Number value) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(FilterCriteria.of(attributeName, FilterOperationEnum.FO_EQUAL, value)));
            return this;
        }

        public FilterExpressionBuilder equalValue(String attributeName, LocalDate value) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(FilterCriteria.of(attributeName, FilterOperationEnum.FO_EQUAL, value)));
            return this;
        }

        public FilterExpressionBuilder equalValue(String attributeName, LocalDateTime value) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(FilterCriteria.of(attributeName, FilterOperationEnum.FO_EQUAL, value)));
            return this;
        }

        public FilterExpressionBuilder nonEquality(String attributeName, String value) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(FilterCriteria.of(attributeName, FilterOperationEnum.FO_NOT_EQUAL, value)));
            return this;
        }

        public FilterExpressionBuilder nonEquality(String attributeName, Number value) {
            if (this.children == null) {
                this.children = new ArrayList();
            }

            this.children.add(new FilterExpression(FilterCriteria.of(attributeName, FilterOperationEnum.FO_NOT_EQUAL, value)));
            return this;
        }

        public FilterExpression build() {
            List resultChildren;
            switch (this.children == null ? 0 : this.children.size()) {
                case 0:
                    resultChildren = Collections.emptyList();
                    break;
                case 1:
                    resultChildren = Collections.singletonList((FilterExpression)((ArrayList)Objects.requireNonNull(this.children)).get(0));
                    break;
                default:
                    resultChildren = Collections.unmodifiableList(new ArrayList(this.children));
            }

            return this.value == null && resultChildren.size() == 1 ? (FilterExpression)resultChildren.get(0) : new FilterExpression(this.value, this.chainOperation, resultChildren);
        }

        public String toString() {
            return "FilterExpression.FilterExpressionBuilder(value=" + this.value + ", chainOperation=" + this.chainOperation + ", children=" + this.children + ")";
        }
    }
}
