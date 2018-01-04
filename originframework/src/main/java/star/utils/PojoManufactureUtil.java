package star.utils;

import star.annotation.manufacture.DateType;
import star.annotation.manufacture.EnumType;
import star.bean.TypeWrapper;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collection;

import static star.utils.NumberUtil.getRandomNum;

/**
 * @author keshawn
 * @date 2018/1/4
 */
public final class PojoManufactureUtil {

    private PojoManufactureUtil() {
    }

    private static final PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    public static Object manufacture(Type type) {
        TypeWrapper typeWrapper = ReflectionUtil.typeParse(type);
        return typeWrapper.isCollection()
                ? collectionPojoBuild(typeWrapper.getCls(), typeWrapper.getGenericType())
                : pojoBuild(typeWrapper.getCls());
    }

    private static <T> T pojoBuild(Class<T> pojoClass) {
        T pojo = PODAM_FACTORY.manufacturePojo(pojoClass);
        fieldCustom(pojo);
        return pojo;
    }

    private static <T> T collectionPojoBuild(Class<T> pojoClass, Type... genericTypeArgs) {
        T t = PODAM_FACTORY.manufacturePojo(pojoClass, genericTypeArgs);
        for (Object pojo : (Collection) t) {
            fieldCustom(pojo);
        }
        return t;
    }

    private static void fieldCustom(Object pojo) {
        Class<?> pojoClass = pojo.getClass();
        Field[] fields = pojoClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(EnumType.class)) {
                EnumType enumType = field.getAnnotation(EnumType.class);
                String[] value = enumType.value();
                ReflectionUtil.setField(pojo, field, value[getRandomNum(value.length)]);
            }
            if (field.isAnnotationPresent(DateType.class)) {
                DateType dateType = field.getAnnotation(DateType.class);
                String dateString = DateUtil.toString(LocalDateTime.now(), dateType.value());
                ReflectionUtil.setField(pojo, field, dateString);
            }
        }
    }
}
