package star.bean;

import java.lang.reflect.Type;

public class ParameterType<T> {

    private Type type;

    public ParameterType() {
    }

    public ParameterType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
