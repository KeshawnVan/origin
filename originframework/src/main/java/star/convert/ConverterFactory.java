package star.convert;

import com.google.common.collect.Lists;
import star.annotation.bean.ExcelColumn;
import star.utils.CollectionUtil;
import star.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConverterFactory {

    private static final ConverterFactory INSTANCE = new ConverterFactory();

    private ConverterFactory() {
    }

    public static ConverterFactory getINSTANCE() {
        return INSTANCE;
    }

    public Class getConverter(Field field) {
        if (field.isAnnotationPresent(ExcelColumn.class)) {
            ExcelColumn excelColumn = field.getAnnotation(ExcelColumn.class);
            Class converter = excelColumn.converter();
            if (converter.equals(AutoConverter.class)) {
                List<Class> allConverter = getAllConverter();
                Map<Type, List<ClassAndGenericType>> type2convertersMap = allConverter.stream()
                        .map(this::buildClassAndGenericType)
                        .filter(classAndGenericType -> classAndGenericType.getGenericType() != null)
                        .collect(Collectors.groupingBy(ClassAndGenericType::getGenericType));
                Class<?> fieldType = field.getType();
                List<ClassAndGenericType> classAndGenericTypes = type2convertersMap.get(fieldType);
                if (CollectionUtil.isEmpty(classAndGenericTypes)) return null;
                // 找到多个打印日志
                return classAndGenericTypes.get(0).getClazz();
            } else {
                return converter;
            }
        }
        return null;
    }

    private ClassAndGenericType buildClassAndGenericType(Class clazz) {
        ClassAndGenericType classAndGenericType = new ClassAndGenericType();
        classAndGenericType.setClazz(clazz);
        Type[] actualTypeArguments = getActualTypeArguments(clazz);
        if (actualTypeArguments != null && actualTypeArguments.length > 0) {
            classAndGenericType.setGenericType(actualTypeArguments[0]);
        }
        return classAndGenericType;
    }

    private Type[] getActualTypeArguments(Class<?> cls) {
        Type[] genericInterfaces = cls.getGenericInterfaces();
        return ReflectionUtil.getActualTypeArguments(genericInterfaces[0]);
    }

    /**
     * 根据包扫描获取class，之后判断接口是否为ExcelConverter
     * 这里的实现暂时省略
     *
     * @return
     */
    public List<Class> getAllConverter() {
        return Lists.newArrayList(String2DateConverter.class, Long2DoubleConverter.class);
    }

    class ClassAndGenericType {
        Class clazz;
        Type genericType;

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        public Type getGenericType() {
            return genericType;
        }

        public void setGenericType(Type genericType) {
            this.genericType = genericType;
        }
    }

}
