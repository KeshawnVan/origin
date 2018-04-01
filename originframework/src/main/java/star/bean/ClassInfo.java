package star.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ClassInfo {

    private List<Field> fields;

    private List<Method> methods;

    private List<String> getMethodNames;

    private List<String> setMethodNames;

    private Map<String, Field> fieldMap;

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    public List<String> getGetMethodNames() {
        return getMethodNames;
    }

    public void setGetMethodNames(List<String> getMethodNames) {
        this.getMethodNames = getMethodNames;
    }

    public List<String> getSetMethodNames() {
        return setMethodNames;
    }

    public void setSetMethodNames(List<String> setMethodNames) {
        this.setMethodNames = setMethodNames;
    }

    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, Field> fieldMap) {
        this.fieldMap = fieldMap;
    }
}
