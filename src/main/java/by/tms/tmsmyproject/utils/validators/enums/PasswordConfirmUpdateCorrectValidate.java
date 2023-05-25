package by.tms.tmsmyproject.utils.validators.enums;

import by.tms.tmsmyproject.utils.currentuser.CurrentUserUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@AllArgsConstructor
public class PasswordConfirmUpdateCorrectValidate implements ConstraintValidator<PasswordConfirmUpdateCorrect, String> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return (passwordEncoder.matches(password, CurrentUserUtils.getUser().getPassword()));
    }
}
