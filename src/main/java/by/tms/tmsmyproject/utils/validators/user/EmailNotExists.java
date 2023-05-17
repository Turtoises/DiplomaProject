package by.tms.tmsmyproject.utils.validators.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailNotExistsValidate.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailNotExists {
    String message() default "This email is already used!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
