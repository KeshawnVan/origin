package star.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import star.annotation.bean.Transfer;
import star.bean.TypeWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * @author keshawn
 * @date 2018/3/8
 */
public final class BeanUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);

    private BeanUtil() {
    }

    public static <T> T copyProperties(Object source, Class<T> targetClass){
        T target = ReflectionUtil.newInstance(targetClass);
        copyProperties(source, target);
        return target;
    }

    public static void copyProperties(Object source, Object target){
        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        Field[] sourceClassDeclaredFields = sourceClass.getDeclaredFields();

        doCopy(source, target, targetClass, sourceClassDeclaredFields);
    }

    private static void doCopy(Object source, Object target, Class<?> targetClass, Field[] sourceClassDeclaredFields) {
        for (int i = 0; i < sourceClassDeclaredFields.length; i++) {
            try {
                Field sourceField = sourceClassDeclaredFields[i];
                Object sourceFieldValue = ReflectionUtil.getField(sourceField, source);
                //sourceFieldValue为空的不需要复制
                if (sourceFieldValue != null){
                    String targetFieldName = getTargetFieldName(sourceField);

                    Field targetField = targetClass.getDeclaredField(targetFieldName);
                    //如果目标字段类型是源字段类型或其接口，直接赋值
                    //Determines if the class or interface represented by this
                    if (targetField.getType().isAssignableFrom(sourceField.getType())){
                        ReflectionUtil.setField(target, targetField, sourceFieldValue);
                    }else {
                        //否则看sourceField是否为String，使用JsonUtil直接反序列化
                        parseStringValue(target, sourceField, sourceFieldValue, targetField);
                    }
                }
            } catch (NoSuchFieldException e) {
                LOGGER.error("BeanUtil copyProperties cannot find field", e);
            }
        }
    }

    private static void parseStringValue(Object target, Field sourceField, Object sourceFieldValue, Field targetField) {
        if (sourceField.getType().equals(String.class)){
            String sourceStringValue = CastUtil.castString(sourceFieldValue);
            //判断targetField的类型，如果是不是集合直接赋值，否则序列化成List
            Type targetFieldGenericType = targetField.getGenericType();
            TypeWrapper targetFieldTypeWrapper = ReflectionUtil.typeParse(targetFieldGenericType);
            Object targetFieldValue = targetFieldTypeWrapper.isCollection()
                    ? JsonUtil.decodeArrayJson(sourceStringValue, (Class<? extends Object>) targetFieldTypeWrapper.getGenericType()[0])
                    : JsonUtil.decodeJson(sourceStringValue, targetFieldTypeWrapper.getCls());
            ReflectionUtil.setField(target, targetField, targetFieldValue);
        }
    }

    private static String getTargetFieldName(Field sourceField) {
        //如果字段上使用了@Transfer则取出value看是否为空字符串，如果不为空则取value，否则取字段名
        return sourceField.isAnnotationPresent(Transfer.class)
                ? StringUtil.isNotEmpty(sourceField.getAnnotation(Transfer.class).value()) ? sourceField.getAnnotation(Transfer.class).value() : sourceField.getName()
                : sourceField.getName();
    }
}