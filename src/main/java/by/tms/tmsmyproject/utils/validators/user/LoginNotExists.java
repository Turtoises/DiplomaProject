package by.tms.tmsmyproject.utils.validators.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoginNotExistsValidate.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginNotExists {
    String message() default "This login is already used!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
