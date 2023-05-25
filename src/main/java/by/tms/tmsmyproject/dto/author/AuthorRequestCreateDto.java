package by.tms.tmsmyproject.dto.author;

import by.tms.tmsmyproject.dto.book.BookResponseWithoutAuthorDto;
import by.tms.tmsmyproject.utils.constants.ConstantsRegex;
import by.tms.tmsmyproject.utils.validators.author.AuthorNotExists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@AuthorNotExists
public class AuthorRequestCreateDto extends AuthorDto {

    @Pattern(regexp = ConstantsRegex.NAME_AUTHOR, message = "Name incorrect")
    private String name;
    @Pattern(regexp = ConstantsRegex.NAME_AUTHOR, message = "Surname incorrect")
    private String surname;
    @Min(-2000)
    @Max(2022)
    private Integer birthYear;
    @Min(-2000)
    @Max(2022)
    private Integer deathYear;
    private List<BookResponseWithoutAuthorDto> books;
}
