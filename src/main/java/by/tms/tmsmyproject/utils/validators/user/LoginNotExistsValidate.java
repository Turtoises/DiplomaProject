package by.tms.tmsmyproject.utils.validators.user;

import by.tms.tmsmyproject.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@AllArgsConstructor
public class LoginNotExistsValidate implements ConstraintValidator<LoginNotExists, String> {

    UserService userService;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext constraintValidatorContext) {
        return (login != null && !userService.isUserLogin(login));
    }
}
