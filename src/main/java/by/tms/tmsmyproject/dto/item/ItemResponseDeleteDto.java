package by.tms.tmsmyproject.dto.item;

import by.tms.tmsmyproject.dto.user.UserResponseGetDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDeleteDto extends ItemDto{

    private Long id;
    private LocalDateTime dateCreate;
    private UserResponseGetDto user;

}
