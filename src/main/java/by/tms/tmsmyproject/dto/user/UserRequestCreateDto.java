package by.tms.tmsmyproject.dto.user;

import by.tms.tmsmyproject.enums.RoleUser;
import by.tms.tmsmyproject.utils.constants.ConstantsRegex;
import by.tms.tmsmyproject.utils.validators.enums.ValueOfEnum;
import by.tms.tmsmyproject.utils.validators.user.EmailNotExists;
import by.tms.tmsmyproject.utils.validators.user.LoginNotExists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestCreateDto extends UserDto {

    @LoginNotExists
    @NotNull
    @Pattern(regexp = ConstantsRegex.LOGIN, message = "Login does not comply with site rules")
    @Size(min = 3, max = 15, message = "The length of the login should be between 3 and 15")
    private String login;
    @NotNull
    @Pattern(regexp = ConstantsRegex.PASSWORD, message = "Password does not comply with site rules")
    @Size(min = 5, max = 25, message = "The length of the login should be between 5 and 15")
    private String password;
    @ValueOfEnum(enumClass = RoleUser.class, message = "Role incorrect")
    private String role;
    @EmailNotExists
    @NotNull
    @Email(message = "Email incorrect")
    private String email;
    @NotNull
    @Pattern(regexp = ConstantsRegex.NAME_USER, message = "Name incorrect")
    private String name;
    @NotNull
    @Pattern(regexp = ConstantsRegex.NAME_USER, message = "Surname incorrect")
    private String surname;

}
