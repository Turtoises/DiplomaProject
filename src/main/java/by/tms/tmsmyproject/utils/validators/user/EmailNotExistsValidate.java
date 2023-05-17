package by.tms.tmsmyproject.utils.validators.user;

import by.tms.tmsmyproject.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@AllArgsConstructor
public class EmailNotExistsValidate implements ConstraintValidator<EmailNotExists, String> {

    UserService userService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return (email != null && !userService.isUserEmail(email));
    }
}
