package by.tms.tmsmyproject.utils.validators.user;

import by.tms.tmsmyproject.utils.constants.ConstantsRegex;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@AllArgsConstructor
public class PasswordAdminUpdateCorrectValidate implements ConstraintValidator<PasswordAdminUpdateCorrect, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return (password.isEmpty() || password.matches(ConstantsRegex.PASSWORD));
    }
}
