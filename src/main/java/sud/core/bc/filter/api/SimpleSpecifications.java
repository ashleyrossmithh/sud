package sud.core.bc.filter.api;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

public class SimpleSpecifications {
    private static final String TRUNC_FUNCTION = "TRUNC";

    public SimpleSpecifications() {
    }

    public static <V> Specification<V> isEqual(String attributeName, Object value, Path<? extends Expression<?>> path, String anotherAttributeName, boolean notIgnoreCase) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Expression<String> attr = getAttributeToCompare(root, path, attributeName);
            if (anotherAttributeName != null) {
                return criteriaBuilder.equal(getAttributeToCompare(root, path, attributeName), getAttributeToCompare(root, path, anotherAttributeName));
            } else if (value == null) {
                return null;
            } else {
                return !notIgnoreCase && attr.getJavaType().equals(String.class) ? criteriaBuilder.equal(criteriaBuilder.upper(attr), getAttributePathOrValueToCompare(root, path, attributeName, value.toString().toUpperCase(), anotherAttributeName)) : criteriaBuilder.equal(attr, getAttributePathOrValueToCompare(root, path, attributeName, value, anotherAttributeName));
            }
        };
    }

    public static <V> Specification<V> isLocalDatesEqualIgnoreTime(String attributeName, LocalDateTime value, From<?, ?> path, String anotherAttributeName) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Expression<?> attr = getAttributeToCompare(root, path, attributeName);
            if (anotherAttributeName != null) {
                return criteriaBuilder.equal(criteriaBuilder.function("TRUNC", LocalDateTime.class, new Expression[]{getAttributeToCompare(root, path, attributeName)}), criteriaBuilder.function("TRUNC", LocalDateTime.class, new Expression[]{getAttributeToCompare(root, path, anotherAttributeName)}));
            } else {
                return value == null ? null : criteriaBuilder.equal(criteriaBuilder.function("TRUNC", LocalDateTime.class, new Expression[]{attr}), getAttributePathOrValueToCompare(root, path, attributeName, value.withHour(0).withMinute(0).withSecond(0), anotherAttributeName));
            }
        };
    }

    public static <V> Specification<V> isNull(String attributeName, From<?, ?> path) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Expression<?> attr = getAttributeToCompare(root, path, attributeName);
            return criteriaBuilder.isNull(attr);
        };
    }

    public static <V> Specification<V> isNotEqual(String attributeName, Object value, Path<? extends Expression<?>> path, String anotherAttributeName, boolean notIgnoreCase) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Expression<String> attr = getAttributeToCompare(root, path, attributeName);
            if (anotherAttributeName != null) {
                return criteriaBuilder.notEqual(attr, getAttributeToCompare(root, path, anotherAttributeName));
            } else if (value == null) {
                return null;
            } else {
                return !notIgnoreCase && attr.getJavaType().equals(String.class) ? criteriaBuilder.notEqual(criteriaBuilder.upper(attr), getValueToCompare(attr, value.toString().toUpperCase())) : criteriaBuilder.notEqual(attr, getValueToCompare(attr, value));
            }
        };
    }

    public static <V> Specification<V> isNotNull(String attributeName, From<?, ?> path) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Expression<?> attr = getAttributeToCompare(root, path, attributeName);
            return criteriaBuilder.isNotNull(attr);
        };
    }

    private static Predicate localDateTimePredicate(LocalDateTime value, Predicate predicate, Predicate anotherAttrPredicate) {
        return objectPredicate(value, predicate, anotherAttrPredicate);
    }

    private static Predicate localDatePredicate(LocalDate value, Predicate predicate, Predicate anotherAttrPredicate) {
        return objectPredicate(value, predicate, anotherAttrPredicate);
    }

    private static Predicate numberPredicate(Number value, Predicate predicate, Predicate anotherAttrPredicate) {
        return objectPredicate(value, predicate, anotherAttrPredicate);
    }

    private static Predicate objectPredicate(Object value, Predicate predicate, Predicate anotherAttrPredicate) {
        if (value == null && anotherAttrPredicate == null) {
            return null;
        } else {
            return anotherAttrPredicate == null ? predicate : anotherAttrPredicate;
        }
    }

    private static Predicate compareLocalDateTimePredicate(Expression<? extends Comparable<?>> attributePath, Expression<? extends Comparable<?>> anotherAttributePath, LocalDateTime value, CriteriaBuilder criteriaBuilder, String function) {
        CompareFunctionEnum compareFunction = SimpleSpecifications.CompareFunctionEnum.fromString(function);
        if (compareFunction == null) {
            return null;
        } else {
            Predicate var10000;
            switch (compareFunction) {
/*                case LESS_THAN:
                    var10000 = localDateTimePredicate(value, criteriaBuilder.lessThan(attributePath, value), anotherAttributePath == null ? null : criteriaBuilder.lessThan(attributePath, anotherAttributePath));
                    break;
                case LESS_THAN_OR_EQUAL_TO:
                    var10000 = localDateTimePredicate(value, criteriaBuilder.lessThanOrEqualTo(attributePath, value), anotherAttributePath == null ? null : criteriaBuilder.lessThanOrEqualTo(attributePath, anotherAttributePath));
                    break;
                case GREATER_THAN:
                    var10000 = localDateTimePredicate(value, criteriaBuilder.greaterThan(attributePath, value), anotherAttributePath == null ? null : criteriaBuilder.greaterThan(attributePath, anotherAttributePath));
                    break;
                case GREATER_THAN_OR_EQUAL_TO:
                    var10000 = localDateTimePredicate(value, criteriaBuilder.greaterThanOrEqualTo(attributePath, value), anotherAttributePath == null ? null : criteriaBuilder.greaterThanOrEqualTo(attributePath, anotherAttributePath));
                    break;*/
                default:
                    throw new IncompatibleClassChangeError();
            }

        }
    }

    private static Predicate compareLocalDatePredicate(Expression<? extends Comparable<?>> attributePath, Expression<?> anotherAttributePath, LocalDate value, CriteriaBuilder criteriaBuilder, String function) {
        CompareFunctionEnum compareFunction = SimpleSpecifications.CompareFunctionEnum.fromString(function);
        if (compareFunction == null) {
            return null;
        } else {
            Predicate var10000;
            switch (compareFunction) {
        /*        case LESS_THAN:
                    var10000 = localDatePredicate(value, criteriaBuilder.lessThan(attributePath, value), anotherAttributePath == null ? null : criteriaBuilder.lessThan(attributePath, anotherAttributePath));
                    break;
                case LESS_THAN_OR_EQUAL_TO:
                    var10000 = localDatePredicate(value, criteriaBuilder.lessThanOrEqualTo(attributePath, value), anotherAttributePath == null ? null : criteriaBuilder.lessThanOrEqualTo(attributePath, anotherAttributePath));
                    break;
                case GREATER_THAN:
                    var10000 = localDatePredicate(value, criteriaBuilder.greaterThan(attributePath, value), anotherAttributePath == null ? null : criteriaBuilder.greaterThan(attributePath, anotherAttributePath));
                    break;
                case GREATER_THAN_OR_EQUAL_TO:
                    var10000 = localDatePredicate(value, criteriaBuilder.greaterThanOrEqualTo(attributePath, value), anotherAttributePath == null ? null : criteriaBuilder.greaterThanOrEqualTo(attributePath, anotherAttributePath));
                    break;*/
                default:
                    throw new IncompatibleClassChangeError();
            }

        }
    }

    private static Predicate compareNumberPredicate(Expression<? extends Comparable<?>> attributePath, Expression<? extends Comparable<?>> anotherAttributePath, Number value, CriteriaBuilder criteriaBuilder, String function) {
        CompareFunctionEnum compareFunction = SimpleSpecifications.CompareFunctionEnum.fromString(function);
        if (compareFunction == null) {
            return null;
        } else {
            Predicate var10000;
            switch (compareFunction) {
           /*     case LESS_THAN:
                    var10000 = numberPredicate(value, criteriaBuilder.lt((Expression<? extends Number>) attributePath, value), anotherAttributePath == null ? null : criteriaBuilder.lt(attributePath, anotherAttributePath));
                    break;
                case LESS_THAN_OR_EQUAL_TO:
                    var10000 = numberPredicate(value, criteriaBuilder.le((Expression<? extends Number>) attributePath, value), anotherAttributePath == null ? null : criteriaBuilder.le((Expression<? extends Number>) attributePath, anotherAttributePath));
                    break;
                case GREATER_THAN:
                    var10000 = numberPredicate(value, criteriaBuilder.gt((Expression<? extends Number>) attributePath, value), anotherAttributePath == null ? null : criteriaBuilder.gt((Expression<? extends Number>) attributePath, anotherAttributePath));
                    break;
                case GREATER_THAN_OR_EQUAL_TO:
                    var10000 = numberPredicate(value, criteriaBuilder.ge((Expression<? extends Number>) attributePath, value), anotherAttributePath == null ? null : criteriaBuilder.ge((Expression<? extends Number>) attributePath, anotherAttributePath));
                    break;*/
                default:
                    throw new IncompatibleClassChangeError();
            }

        }
    }

    private static <V> Specification<V> compareSpecification(String attributeName, String anotherAttributeName, Object value, From<?, ?> path, String function) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Expression<? extends Comparable<?>> attr = getAttributeToCompare(root, path, attributeName);
            Expression<? extends Comparable<?>> anotherAttr = null;
            if (anotherAttributeName != null) {
                anotherAttr = getAttributeToCompare(root, path, anotherAttributeName);
            }

            if (attr.getJavaType().equals(LocalDateTime.class)) {
                return compareLocalDateTimePredicate(attr, anotherAttr, (LocalDateTime)value, criteriaBuilder, function);
            } else {
                return attr.getJavaType().equals(LocalDate.class) ? compareLocalDatePredicate(attr, anotherAttr, (LocalDate)value, criteriaBuilder, function) : compareNumberPredicate(attr, anotherAttr, (Number)value, criteriaBuilder, function);
            }
        };
    }

    public static <V> Specification<V> isLessThan(String attributeName, Object value, From<?, ?> path, String anotherAttributeName) {
        return compareSpecification(attributeName, anotherAttributeName, value, path, "lessThan");
    }

    public static <V> Specification<V> isLessThanOrEqual(String attributeName, Object value, From<?, ?> path, String anotherAttributeName) {
        return compareSpecification(attributeName, anotherAttributeName, value, path, "lessThanOrEqualTo");
    }

    public static <V> Specification<V> isGreaterThan(String attributeName, Object value, From<?, ?> path, String anotherAttributeName) {
        return compareSpecification(attributeName, anotherAttributeName, value, path, "greaterThan");
    }

    public static <V> Specification<V> isGreaterThanOrEqual(String attributeName, Object value, From<?, ?> path, String anotherAttributeName) {
        return compareSpecification(attributeName, anotherAttributeName, value, path, "greaterThanOrEqualTo");
    }

    public static <V> Specification<V> containsLike(String attributeName, String value, From<?, ?> path, String anotherAttributeName, boolean notIgnoreCase) {
        return (root, query, cb) -> {
            if (anotherAttributeName != null) {
                return cb.like(getAttributeToCompare(root, path, attributeName), getAttributeToCompare(root, path, anotherAttributeName));
            } else if (value == null) {
                return null;
            } else {
                return notIgnoreCase ? cb.like(getAttributeToCompare(root, path, attributeName), "%" + value + "%") : cb.like(cb.upper(getAttributeToCompare(root, path, attributeName)), "%" + value.toUpperCase() + "%");
            }
        };
    }

    public static <V> Specification<V> beginsWith(String attributeName, String value, From<?, ?> path, String anotherAttributeName, boolean notIgnoreCase) {
        return (root, query, cb) -> {
            if (anotherAttributeName != null) {
                return cb.like(getAttributeToCompare(root, path, attributeName), getAttributeToCompare(root, path, anotherAttributeName));
            } else if (value == null) {
                return null;
            } else {
                return notIgnoreCase ? cb.like(getAttributeToCompare(root, path, attributeName), value + "%") : cb.like(cb.upper(getAttributeToCompare(root, path, attributeName)), value.toUpperCase() + "%");
            }
        };
    }

    public static <V> Specification<V> notBeginsWith(String attributeName, String value, From<?, ?> path, String anotherAttributeName, boolean notIgnoreCase) {
        return (root, query, cb) -> {
            if (anotherAttributeName != null) {
                return cb.like(getAttributeToCompare(root, path, attributeName), getAttributeToCompare(root, path, anotherAttributeName));
            } else if (value == null) {
                return null;
            } else {
                return notIgnoreCase ? cb.notLike(getAttributeToCompare(root, path, attributeName), value + "%") : cb.notLike(cb.upper(getAttributeToCompare(root, path, attributeName)), value.toUpperCase() + "%");
            }
        };
    }

    public static <V> Specification<V> endsWith(String attributeName, String value, From<?, ?> path, String anotherAttributeName, boolean notIgnoreCase) {
        return (root, query, cb) -> {
            if (anotherAttributeName != null) {
                return cb.like(getAttributeToCompare(root, path, attributeName), getAttributeToCompare(root, path, anotherAttributeName));
            } else if (value == null) {
                return null;
            } else {
                return notIgnoreCase ? cb.like(getAttributeToCompare(root, path, attributeName), "%" + value) : cb.like(cb.upper(getAttributeToCompare(root, path, attributeName)), "%" + value.toUpperCase());
            }
        };
    }

    public static <V> Specification<V> notEndsWith(String attributeName, String value, From<?, ?> path, String anotherAttributeName, boolean notIgnoreCase) {
        return (root, query, cb) -> {
            if (anotherAttributeName != null) {
                return cb.like(getAttributeToCompare(root, path, attributeName), getAttributeToCompare(root, path, anotherAttributeName));
            } else if (value == null) {
                return null;
            } else {
                return notIgnoreCase ? cb.notLike(getAttributeToCompare(root, path, attributeName), "%" + value) : cb.notLike(cb.upper(getAttributeToCompare(root, path, attributeName)), "%" + value.toUpperCase());
            }
        };
    }

    public static <V> Specification<V> lengthEquals(String attributeName, Integer value, From<?, ?> path, String anotherAttributeName) {
        return (root, query, cb) -> {
            if (anotherAttributeName != null) {
                return cb.equal(cb.length(getAttributeToCompare(root, path, attributeName)), getAttributeToCompare(root, path, anotherAttributeName));
            } else {
                return value == null ? null : cb.equal(cb.length(getAttributeToCompare(root, path, attributeName)), value);
            }
        };
    }

    public static <V> Specification<V> lengthNotEquals(String attributeName, Integer value, From<?, ?> path, String anotherAttributeName) {
        return (root, query, cb) -> {
            if (anotherAttributeName != null) {
                return cb.equal(cb.length(getAttributeToCompare(root, path, attributeName)), getAttributeToCompare(root, path, anotherAttributeName));
            } else {
                return value == null ? null : cb.notEqual(cb.length(getAttributeToCompare(root, path, attributeName)), value);
            }
        };
    }

    private static <T, V> Expression<T> lengthCompareHelper(Root<V> root, From<?, ?> path, String anotherAttributeName) {
        return (Expression<T>) getAttributeToCompare(root, path, anotherAttributeName);
    }

    public static <V> Specification<V> lengthGreater(String attributeName, Integer value, From<?, ?> path, String anotherAttributeName) {
        return (root, query, cb) -> {
            if (anotherAttributeName != null) {
                return cb.gt(cb.length(getAttributeToCompare(root, path, attributeName)), lengthCompareHelper(root, path, anotherAttributeName));
            } else {
                return value == null ? null : cb.gt(cb.length(getAttributeToCompare(root, path, attributeName)), value);
            }
        };
    }

    public static <V> Specification<V> lengthGreaterOrEquals(String attributeName, Integer value, From<?, ?> path, String anotherAttributeName) {
        return (root, query, cb) -> {
            if (anotherAttributeName != null) {
                return cb.gt(cb.length(getAttributeToCompare(root, path, attributeName)), lengthCompareHelper(root, path, anotherAttributeName));
            } else {
                return value == null ? null : cb.ge(cb.length(getAttributeToCompare(root, path, attributeName)), value);
            }
        };
    }

    public static <V> Specification<V> lengthLess(String attributeName, Integer value, From<?, ?> path, String anotherAttributeName) {
        return (root, query, cb) -> {
            if (anotherAttributeName != null) {
                return cb.le(cb.length(getAttributeToCompare(root, path, attributeName)), lengthCompareHelper(root, path, anotherAttributeName));
            } else {
                return value == null ? null : cb.lt(cb.length(getAttributeToCompare(root, path, attributeName)), value);
            }
        };
    }

    public static <V> Specification<V> lengthLessOrEquals(String attributeName, Integer value, From<?, ?> path, String anotherAttributeName) {
        return (root, query, cb) -> {
            if (anotherAttributeName != null) {
                return cb.le(cb.length(getAttributeToCompare(root, path, attributeName)), lengthCompareHelper(root, path, anotherAttributeName));
            } else {
                return value == null ? null : cb.le(cb.length(getAttributeToCompare(root, path, attributeName)), value);
            }
        };
    }

    public static <V> Specification<V> notContains(String attributeName, String value, From<?, ?> path, String anotherAttributeName, boolean notIgnoreCase) {
        return (root, query, cb) -> {
            if (anotherAttributeName != null) {
                return cb.notLike(getAttributeToCompare(root, path, attributeName), getAttributeToCompare(root, path, anotherAttributeName));
            } else if (value == null) {
                return null;
            } else {
                return notIgnoreCase ? cb.notLike(getAttributeToCompare(root, path, attributeName), "%" + value + "%") : cb.notLike(cb.upper(getAttributeToCompare(root, path, attributeName)), "%" + value.toUpperCase() + "%");
            }
        };
    }

    public static <V> Specification<V> isInList(String attributeName, List<?> value, From<?, ?> path) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Expression<?> attr = getAttributeToCompare(root, path, attributeName);
            if (value != null && !value.isEmpty()) {
                return !attr.getJavaType().isEnum() ? getAttributeToCompare(root, path, attributeName).in(value) : attr.in(new Object[]{getValueToCompare(attr, value)});
            } else {
                return null;
            }
        };
    }

    public static <V> Specification<V> checkSubquery(List<FilterCriteria> criteriaList) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            String firstAttributeName = ((FilterCriteria)criteriaList.get(0)).getAttributeName();
            String fullJoinName = firstAttributeName.substring(0, firstAttributeName.indexOf("*"));
            String[] parts = fullJoinName.split("\\.");
            Join attrJoin;
            if (parts.length > 1) {
                attrJoin = root.join(parts[0]);

                for(int ix = 1; ix < parts.length; ++ix) {
                    attrJoin = attrJoin.join(parts[ix]);
                }
            } else {
                attrJoin = root.join(fullJoinName);
            }

            List<Predicate> predicates = new ArrayList();
            Iterator var9 = criteriaList.iterator();

            while(var9.hasNext()) {
                FilterCriteria criteria = (FilterCriteria)var9.next();
                FilterCriteria newCriteria = new FilterCriteria(criteria.getAttributeName().substring(criteria.getAttributeName().indexOf("*") + 2), criteria.getOperation(), criteria.getValueToCompare(), criteria.getChainOperation(), criteria.getValTypeCode());
                predicates.add(criteria.getOperation().getSpecification(newCriteria, attrJoin).toPredicate(null, criteriaQuery, criteriaBuilder));
            }

            Predicate result = (Predicate)predicates.get(0);

            for(int i = 1; i < predicates.size(); ++i) {
                if (((FilterCriteria)criteriaList.get(i)).getChainOperation() == ChainOperationEnum.CO_AND) {
                    result = criteriaBuilder.and(result, (Expression)predicates.get(i));
                } else {
                    result = criteriaBuilder.or(result, (Expression)predicates.get(i));
                }
            }

            return result;
        };
    }

/*    public static <V> Specification<V> isInSessionDataNumbers(String attributeName, Long sessionBlockId, From<?, ?> path) {
        List<Integer> values = SessionDataGetter.getSessionDataValuesForNumericAttr(sessionBlockId);
        return isInList(attributeName, values, path);
    }*/

/*
    public static <V> Specification<V> isInSessionDataStrs(String attributeName, Long sessionBlockId, From<?, ?> path) {
        List<String> values = SessionDataGetter.getSessionDataValuesForStringAttr(sessionBlockId);
        return isInList(attributeName, values, path);
    }
*/
    private static <T> Expression<T> getAttributeToCompare(Root<?> root, Path<?> from, String attributeName) {
        if (from == null) {
            from = root;
        }

        if (attributeName == null) {
            return null;
        } else if (!attributeName.contains(".")) {
            return ((Path)from).get(attributeName);
        } else {
            String[] parts = attributeName.split("\\.");
            Path<?> value = from;
            String[] var5 = parts;
            int var6 = parts.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String part = var5[var7];
                value = ((Path)value).get(part);
            }

            return (Expression)value;
        }
    }

    private static <V> Expression<String> getAttributeToCompare(Root<V> root, From<?,?> from, String attributeName) {
        if (from == null) {
            from = root;
        }

        if (attributeName == null) {
            return null;
        } else if (!attributeName.contains(".")) {
            return ((Path)from).get(attributeName);
        } else {
            String[] parts = attributeName.split("\\.");
            Path<?> value = from;
            String[] var5 = parts;
            int var6 = parts.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String part = var5[var7];
                value = ((Path)value).get(part);
            }

            return (Expression)value;
        }
    }

    private static <T extends Enum<T>> Enum<T> obtainValueFromEnumByLabel(Class<?> enumClass, String label) {
        return Enum.valueOf(null, label);
    }

    private static Object getValueToCompare(Expression<?> attr, Object value) {
        if (!attr.getJavaType().isEnum()) {
            return value;
        } else {
            Object var10000;
            if (value instanceof List) {
                List listValue = (List)value;
                var10000 = listValue.stream().map((val) -> {
                    return obtainValueFromEnumByLabel(attr.getJavaType(), (String)val);
                }).toList();
            } else {
                var10000 = obtainValueFromEnumByLabel(attr.getJavaType(), (String)value);
            }

            return var10000;
        }
    }

    private static Object getAttributePathOrValueToCompare(Root<?> root, Path<?> from, String attributeName, Object value, String anotherAttributeName) {
        if (anotherAttributeName == null) {
            Expression<?> attr = (Expression) Objects.requireNonNull(getAttributeToCompare(root, from, attributeName));
            return getValueToCompare(attr, value);
        } else {
            Path<?> path = root.get(anotherAttributeName);
            return path.type();
        }
    }

    private static enum CompareFunctionEnum {
        LESS_THAN("lessThan"),
        LESS_THAN_OR_EQUAL_TO("lessThanOrEqualTo"),
        GREATER_THAN("greaterThan"),
        GREATER_THAN_OR_EQUAL_TO("greaterThanOrEqualTo");

        private final String compareFunction;

        private CompareFunctionEnum(String compareFunction) {
            this.compareFunction = compareFunction;
        }

        public static CompareFunctionEnum fromString(String compareFunction) {
            CompareFunctionEnum[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                CompareFunctionEnum compareFunctionEnum = var1[var3];
                if (compareFunctionEnum.getCompareFunction().equals(compareFunction)) {
                    return compareFunctionEnum;
                }
            }

            return null;
        }

        public String getCompareFunction() {
            return this.compareFunction;
        }
    }
}
