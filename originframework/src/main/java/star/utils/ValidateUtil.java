package star.utils;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Iterator;
import java.util.Set;

public final class ValidateUtil {
    public static <T> void validate(T obj) {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(false)
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
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
