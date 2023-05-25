package by.tms.tmsmyproject.utils.validators.book;

import by.tms.tmsmyproject.dto.book.BookDto;
import by.tms.tmsmyproject.dto.book.BookRequestCreateDto;
import by.tms.tmsmyproject.dto.book.BookRequestUpdateDto;
import by.tms.tmsmyproject.entities.Author;
import by.tms.tmsmyproject.entities.Book;
import by.tms.tmsmyproject.mapers.BookMapper;
import by.tms.tmsmyproject.services.AuthorService;
import by.tms.tmsmyproject.services.BookService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class BookNotExistsValidate implements ConstraintValidator<BookNotExists, BookDto> {

    BookService bookService;
    AuthorService authorService;
    BookMapper bookMapper;

    @Override
    public boolean isValid(BookDto bookDtoDto, ConstraintValidatorContext constraintValidatorContext) {
        if (bookDtoDto instanceof BookRequestCreateDto) {
            BookRequestCreateDto book = (BookRequestCreateDto) bookDtoDto;
            Author author = authorService.getByNameAndSurname(book.getAuthor());
            List<BookRequestCreateDto> books = bookMapper.toDtoCreateList(author.getBooks());
            if (books.contains(book)) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("This books by this author already exists in the database")
                        .addPropertyNode("name")
                        .addConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("This books by this author already exists in the database")
                        .addPropertyNode("author")
                        .addConstraintViolation();
                return false;
            }
        } else if (bookDtoDto instanceof BookRequestUpdateDto) {
            Book book = bookMapper.toEntity((BookRequestUpdateDto) bookDtoDto);
            if (bookService.isBook(book) && !Objects.equals(book.getId(), bookService.findByName(book.getName()).getId())) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("This books by this author already exists in the database")
                        .addPropertyNode("name")
                        .addConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("This books by this author already exists in the database")
                        .addPropertyNode("author")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
