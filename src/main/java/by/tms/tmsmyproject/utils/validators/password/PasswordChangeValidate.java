package by.tms.tmsmyproject.utils.validators.password;

import by.tms.tmsmyproject.dto.Password;
import by.tms.tmsmyproject.utils.currentuser.CurrentUserUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@AllArgsConstructor
public class PasswordChangeValidate implements ConstraintValidator<PasswordChange, Password> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValid(Password password, ConstraintValidatorContext constraintValidatorContext) {
        String oldPassword = password.getOldPassword();
        String newPassword = password.getNewPassword();
        String newPasswordRepeat = password.getNewPasswordRepeat();

        if (!passwordEncoder.matches(oldPassword, CurrentUserUtils.getUser().getPassword())) {

            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Existing password is not entered correctly")
                    .addPropertyNode("oldPassword")
                    .addConstraintViolation();
            return false;
        }

        if (!newPassword.equals(newPasswordRepeat)) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("The new password and the repetition of the new password must be equal")
                        .addPropertyNode("newPassword")
                        .addConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("The new password and the repetition of the new password must be equal")
                        .addPropertyNode("newPasswordRepeat")
                        .addConstraintViolation();
                return false;
        }
        return true;
    }
}
