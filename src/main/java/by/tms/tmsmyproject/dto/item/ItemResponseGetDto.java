package by.tms.tmsmyproject.dto.item;

import by.tms.tmsmyproject.dto.book.BookResponseGetDto;
import by.tms.tmsmyproject.dto.user.UserResponseGetDto;
import by.tms.tmsmyproject.enums.StateItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseGetDto extends ItemDto {

    private Long id;
    private LocalDateTime dateCreate;
    private LocalDateTime dateProgress;
    private LocalDateTime dateChange;
    private LocalDateTime dateExecute;
    private LocalDateTime dateCancelled;
    private StateItem state;

    private Double price;

    private List<BookResponseGetDto> books;
    private UserResponseGetDto user;


}
