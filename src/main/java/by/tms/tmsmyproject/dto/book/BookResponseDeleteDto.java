package by.tms.tmsmyproject.dto.book;

import by.tms.tmsmyproject.dto.author.AuthorResponseDeleteDto;
import by.tms.tmsmyproject.dto.author.AuthorResponseGetDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponseDeleteDto extends BookDto{

    private Long id;
    private String name;
    private Integer year;
    private String genreBook;
    private AuthorResponseDeleteDto author;

}
