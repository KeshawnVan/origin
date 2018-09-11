package star.utils;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

public final class ValidateUtil {

    private static Validator validator = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory()
            .getValidator();

    public static <T> void validate(T obj) {

        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        Iterator<ConstraintViolation<T>> constraintViolationIterator = constraintViolations.iterator();
        while (constraintViolationIterator.hasNext()) {
            ConstraintViolation<T> error = constraintViolationIterator.next();
            throw new IllegalArgumentException(new StringBuilder()
                    .append("[")
                    .append(error.getPropertyPath().toString())
                    .append("]")
                    .append(error.getMessage())
                    .toString());
        }
    }
}
