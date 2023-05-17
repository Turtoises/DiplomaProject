package by.tms.tmsmyproject.utils.validators.author;

import by.tms.tmsmyproject.dto.author.AuthorDto;
import by.tms.tmsmyproject.dto.author.AuthorRequestCreateDto;
import by.tms.tmsmyproject.dto.author.AuthorRequestUpdateDto;
import by.tms.tmsmyproject.entities.Author;
import by.tms.tmsmyproject.mapers.AuthorMapper;
import by.tms.tmsmyproject.services.AuthorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@Component
@AllArgsConstructor
public class AuthorNotExistsValidate implements ConstraintValidator<AuthorNotExists, AuthorDto> {

    AuthorService authorService;
    AuthorMapper authorMapper;

    @Override
    public boolean isValid(AuthorDto authorDto, ConstraintValidatorContext constraintValidatorContext) {
        if (authorDto instanceof AuthorRequestCreateDto) {
            Author author = authorMapper.toEntity((AuthorRequestCreateDto) authorDto);
            if (author != null && authorService.isAuthor(author)) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("This author is already exists!")
                        .addPropertyNode("name")
                        .addConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("This author is already exists!")
                        .addPropertyNode("surname")
                        .addConstraintViolation();
                return false;
            }
        } else if (authorDto instanceof AuthorRequestUpdateDto) {
            Author author = authorMapper.toEntity((AuthorRequestUpdateDto) authorDto);
            if (!(!authorService.isAuthor(author) || authorService.isAuthor(author)
                    && (Objects.equals(author.getId(), authorService.getIdByNameAndSurname(author))))) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("This author is already exists!")
                        .addPropertyNode("name")
                        .addConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("This author is already exists!")
                        .addPropertyNode("surname")
                        .addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
