package by.tms.tmsmyproject.utils.validators.author;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AuthorNotExistsValidate.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthorNotExists {
    String message() default "This author is already exists!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
