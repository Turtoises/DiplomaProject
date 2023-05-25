package by.tms.tmsmyproject.utils.validators.book;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BookNotExistsValidate.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface BookNotExists {
    String message() default "This book is already exists!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
