package by.tms.tmsmyproject.listener;

import by.tms.tmsmyproject.entities.User;
import by.tms.tmsmyproject.enums.RoleUser;
import by.tms.tmsmyproject.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CreateUserDefault {

    UserService userService;
    PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    public void createUserIfNotExistsAfterStartup() {
        //create admin default
        if (!userService.isUserWithRole(RoleUser.ROLE_ADMIN.name())) {
            User user = User.builder()
                    .login("admin")
                    .password("admin")
                    .role(RoleUser.ROLE_ADMIN.name())
                    .build();
            userService.create(user);
        }

        //create user default
        if (!userService.isUserLogin("user")) {
            User user = User.builder()
                    .login("user")
                    .password("user")
                    .role(RoleUser.ROLE_USER.name())
                    .name("Oleg")
                    .surname("Ivanov")
                    .email("1234@mail.ru")
                    .build();
            userService.create(user);
        }
    }
}
