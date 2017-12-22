package star.annotation;

import java.lang.annotation.*;

/**
 * @author keshawn
 * @date 2017/12/21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<? extends Annotation> value();
}
