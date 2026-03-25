package sud.core.dto.support;

import java.beans.ConstructorProperties;
import java.time.LocalDateTime;

public class QueryParam {
    private String paramName;
    private Object paramValue;
    private boolean calculated;
    private LocalDateTime dateParamValue;

    private QueryParam(String paramName, Object paramValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    public QueryParam() {
    }

    @ConstructorProperties({"paramName", "paramValue", "calculated", "dateParamValue"})
    public QueryParam(String paramName, Object paramValue, boolean calculated, LocalDateTime dateParamValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.calculated = calculated;
        this.dateParamValue = dateParamValue;
        if (dateParamValue != null) {
            this.paramValue = dateParamValue;
        }

    }

    public static QueryParam of(String paramName, Object paramValue) {
        return new QueryParam(paramName, paramValue);
    }

    public String getParamName() {
        return this.paramName;
    }

    public Object getParamValue() {
        return this.paramValue;
    }

    public boolean isCalculated() {
        return this.calculated;
    }

    public LocalDateTime getDateParamValue() {
        return this.dateParamValue;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public void setParamValue(Object paramValue) {
        this.paramValue = paramValue;
    }

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public void setDateParamValue(LocalDateTime dateParamValue) {
        this.dateParamValue = dateParamValue;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof QueryParam)) {
            return false;
        } else {
            QueryParam other = (QueryParam)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.isCalculated() != other.isCalculated()) {
                return false;
            } else {
                label49: {
                    Object this$paramName = this.getParamName();
                    Object other$paramName = other.getParamName();
                    if (this$paramName == null) {
                        if (other$paramName == null) {
                            break label49;
                        }
                    } else if (this$paramName.equals(other$paramName)) {
                        break label49;
                    }

                    return false;
                }

                Object this$paramValue = this.getParamValue();
                Object other$paramValue = other.getParamValue();
                if (this$paramValue == null) {
                    if (other$paramValue != null) {
                        return false;
                    }
                } else if (!this$paramValue.equals(other$paramValue)) {
                    return false;
                }

                Object this$dateParamValue = this.getDateParamValue();
                Object other$dateParamValue = other.getDateParamValue();
                if (this$dateParamValue == null) {
                    if (other$dateParamValue != null) {
                        return false;
                    }
                } else if (!this$dateParamValue.equals(other$dateParamValue)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof QueryParam;
    }

    public int hashCode() {
        int result = 1;
        result = result * 59 + (this.isCalculated() ? 79 : 97);
        Object $paramName = this.getParamName();
        result = result * 59 + ($paramName == null ? 43 : $paramName.hashCode());
        Object $paramValue = this.getParamValue();
        result = result * 59 + ($paramValue == null ? 43 : $paramValue.hashCode());
        Object $dateParamValue = this.getDateParamValue();
        result = result * 59 + ($dateParamValue == null ? 43 : $dateParamValue.hashCode());
        return result;
    }

    public String toString() {
        String var10000 = this.getParamName();
        return "QueryParam(paramName=" + var10000 + ", paramValue=" + this.getParamValue() + ", calculated=" + this.isCalculated() + ", dateParamValue=" + this.getDateParamValue() + ")";
    }
}
