package by.tms.tmsmyproject.utils.validators.password;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordChangeValidate.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordChange {
    String message() default "It is not possible to change the password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
