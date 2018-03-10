package star.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.annotation.bean.Transfer;
import star.bean.TypeWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static star.utils.JsonUtil.encodeJson;
import static star.utils.ReflectionUtil.getFields;
import static star.utils.StringUtil.castJsonString;
import static star.utils.StringUtil.isNotEmpty;

/**
 * @author keshawn
 * @date 2018/3/8
 */
public final class BeanUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);

    private BeanUtil() {
    }

    public static <T> T copyProperties(Object source, Class<T> targetClass) {
        T target = ReflectionUtil.newInstance(targetClass);
        copyProperties(source, target);
        return target;
    }

    public static void copyProperties(Object source, Object target) {
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        List<Field> sourceClassDeclaredFields = ReflectionUtil.getFields(sourceClass);

        sourceClassDeclaredFields.forEach(fieldTransfer(source, target, targetClass));
    }

    private static Consumer<Field> fieldTransfer(Object source, Object target, Class<?> targetClass) {
        return sourceField -> {
            try {
                Object sourceFieldValue = ReflectionUtil.getField(sourceField, source);
                //sourceFieldValue为空的不需要复制
                if (sourceFieldValue != null) {
                    String targetFieldName = getTargetFieldName(sourceField);
                    Field targetField = targetClass.getDeclaredField(targetFieldName);
                    //Determines if the class or interface represented by this, inject value
                    if (targetField.getType().isAssignableFrom(sourceField.getType())) {
                        ReflectionUtil.setField(target, targetField, sourceFieldValue);
                    } else {
                        //否则看sourceField是否为String，使用JsonUtil直接反序列化
                        String sourceStringValue = sourceField.getType().equals(String.class)
                                ? castJsonString(sourceFieldValue)
                                : castJsonString(encodeJson(sourceFieldValue));
                        parseStringValue(target, targetField, sourceStringValue);
                    }
                }
            } catch (NoSuchFieldException e) {
                LOGGER.error("BeanUtil copyProperties cannot find field", e);
            }
        };
    }

    private static void parseStringValue(Object target, Field targetField, String sourceStringValue) {
        //判断targetField的类型，如果是不是集合直接赋值，否则序列化成List
        Type targetFieldGenericType = targetField.getGenericType();
        TypeWrapper targetFieldTypeWrapper = ReflectionUtil.typeParse(targetFieldGenericType);
        Object targetFieldValue = targetFieldTypeWrapper.isCollection()
                ? JsonUtil.decodeArrayJson(sourceStringValue, (Class<? extends Object>) targetFieldTypeWrapper.getGenericType()[0])
                : JsonUtil.decodeJson(sourceStringValue, targetFieldTypeWrapper.getCls());
        ReflectionUtil.setField(target, targetField, targetFieldValue);
    }


    private static String getTargetFieldName(Field sourceField) {
        //如果字段上使用了@Transfer则取出value看是否为空字符串，如果不为空则取value，否则取字段名
        return sourceField.isAnnotationPresent(Transfer.class)
                ? isNotEmpty(sourceField.getAnnotation(Transfer.class).value()) ? sourceField.getAnnotation(Transfer.class).value() : sourceField.getName()
                : sourceField.getName();
    }

    public static Map<String, Object> toMap(Object obj) {
        return getFields(obj.getClass()).stream().collect(Collectors.toMap(Field::getName, field -> ReflectionUtil.getField(field, obj)));
    }
}
