package by.tms.tmsmyproject.dto.user;

import by.tms.tmsmyproject.enums.RoleUser;
import by.tms.tmsmyproject.utils.constants.ConstantsRegex;
import by.tms.tmsmyproject.utils.validators.enums.ValueOfEnum;
import by.tms.tmsmyproject.utils.validators.user.PasswordAdminUpdateCorrect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestUpdateAdminDto extends UserDto {

    private Long id;
    @PasswordAdminUpdateCorrect
    private String password;

    @ValueOfEnum(enumClass = RoleUser.class, message = "Role incorrect")
    private String role;

    @Pattern(regexp = ConstantsRegex.NAME_USER, message = "Name incorrect")
    private String name;

    @Pattern(regexp = ConstantsRegex.NAME_USER, message = "Surname incorrect")
    private String surname;

    private String login;

    private String email;

}
