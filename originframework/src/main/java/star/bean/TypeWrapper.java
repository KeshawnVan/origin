package star.bean;

import java.lang.reflect.Type;

/**
 * @author keshawn
 * @date 2018/1/4
 */
public final class TypeWrapper {

    private final Class<?> cls;
    private final Type[] genericType;
    private final Boolean isCollection;

    public TypeWrapper(Class<?> cls, Type[] genericType, Boolean isCollection) {
        this.cls = cls;
        this.genericType = genericType;
        this.isCollection = isCollection;
    }

    public Class<?> getCls() {
        return cls;
    }

    public Type[] getGenericType() {
        return genericType;
    }

    public Boolean isCollection() {
        return isCollection;
    }
}
