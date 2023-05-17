package by.tms.tmsmyproject.utils.validators.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordAdminUpdateCorrectValidate.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordAdminUpdateCorrect {
    String message() default "Password incorrect!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
