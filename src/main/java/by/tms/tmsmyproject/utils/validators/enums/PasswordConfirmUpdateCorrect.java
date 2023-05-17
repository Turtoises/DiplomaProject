package by.tms.tmsmyproject.utils.validators.enums;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordConfirmUpdateCorrectValidate.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConfirmUpdateCorrect {
    String message() default "The password entered does not match the user!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
