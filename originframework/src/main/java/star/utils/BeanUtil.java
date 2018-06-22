package star.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.annotation.bean.Ignore;
import star.annotation.bean.Transfer;
import star.bean.ClassInfo;
import star.bean.TypeWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        ClassInfo sourceClassInfo = ClassUtil.getClassInfo(sourceClass);
        ClassInfo targetClassInfo = ClassUtil.getClassInfo(targetClass);
        List<Field> sourceClassDeclaredFields = sourceClassInfo.getFields().stream()
                .filter(field -> !field.isAnnotationPresent(Ignore.class)).collect(Collectors.toList());
        Map<String, Field> targetClassInfoFieldMap = targetClassInfo.getFieldMap();

        sourceClassDeclaredFields.forEach(fieldTransfer(source, target, targetClass, targetClassInfoFieldMap));
    }

    private static Consumer<Field> fieldTransfer(Object source, Object target, Class<?> targetClass, Map<String, Field> targetClassInfoFieldMap) {
        return sourceField -> {
            Object sourceFieldValue = ReflectionUtil.getField(sourceField, source);
            //sourceFieldValue为空的不需要复制
            if (sourceFieldValue != null) {
                String targetFieldName = getTargetFieldName(sourceField);
                if (targetClassInfoFieldMap.containsKey(targetFieldName)) {
                    transfer(target, sourceField, sourceFieldValue, targetClassInfoFieldMap.get(targetFieldName));
                } else {
                    LOGGER.warn("cannot find targetField {} in {} ", targetFieldName, targetClass);
                }
            }
        };
    }

    private static void transfer(Object target, Field sourceField, Object sourceFieldValue, Field targetField) {
        //Determines if the class or interface represented by this, inject value
        if (targetField.getType().equals(sourceField.getType()) || targetField.getType().isAssignableFrom(sourceField.getType())) {
            ReflectionUtil.setField(target, targetField, sourceFieldValue);
        } else {
            //否则看sourceField是否为String，使用JsonUtil直接反序列化
            String sourceStringValue = sourceField.getType().equals(String.class)
                    ? castJsonString(sourceFieldValue)
                    : encodeJson(sourceFieldValue);
            if (StringUtil.isNotEmpty(sourceStringValue)) {
                parseStringValue(target, targetField, sourceStringValue);
            }
        }
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

    public static Boolean checkAllFieldIsNull(Object bean) {
        return Objects.isNull(bean)
                ? Boolean.TRUE
                : ClassUtil.getClassInfo(bean.getClass()).getFieldMap().values().stream()
                .map(field -> ReflectionUtil.getField(field, bean))
                .noneMatch(Objects::nonNull);
    }
}
