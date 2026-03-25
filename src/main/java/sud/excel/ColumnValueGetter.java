package sud.excel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ColumnValueGetter {

    Method method;
    Field field;
    ColumnValueGetter fieldValueGetter;

    public ColumnValueGetter(Method method) {
        this.method = method;
    }

    public ColumnValueGetter(Field field) {
        this.field = field;
    }

    public ColumnValueGetter(Method method, ColumnValueGetter columnValueGetter) {
        this.method = method;
        this.fieldValueGetter = columnValueGetter;
    }

    public ColumnValueGetter(Field field, ColumnValueGetter columnValueGetter) {
        this.field = field;
        this.fieldValueGetter = columnValueGetter;
    }

    public void setFieldValueGetter(ColumnValueGetter fieldValueGetter) {
        this.fieldValueGetter = fieldValueGetter;
    }

    public Object get(Object rowObj) throws IllegalAccessException, InvocationTargetException {
        Object res = method != null ? method.invoke(rowObj) : field.get(rowObj);
        if (fieldValueGetter != null) {
            return fieldValueGetter.get(res);
        }
        return res;
    }
}
