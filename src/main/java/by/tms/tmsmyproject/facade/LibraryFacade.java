package by.tms.tmsmyproject.facade;

import by.tms.tmsmyproject.dto.PageRequestObject;
import by.tms.tmsmyproject.dto.Password;
import by.tms.tmsmyproject.dto.author.AuthorDto;
import by.tms.tmsmyproject.dto.author.AuthorResponseCreateDto;
import by.tms.tmsmyproject.dto.author.AuthorResponseDeleteDto;
import by.tms.tmsmyproject.dto.author.AuthorResponseGetDto;
import by.tms.tmsmyproject.dto.book.BookDto;
import by.tms.tmsmyproject.dto.book.BookRequestCreateDto;
import by.tms.tmsmyproject.dto.book.BookResponseGetDto;
import by.tms.tmsmyproject.dto.item.ItemDto;
import by.tms.tmsmyproject.dto.item.ItemResponseGetDto;
import by.tms.tmsmyproject.dto.user.*;
import by.tms.tmsmyproject.entities.Author;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;

import java.util.List;

public interface LibraryFacade {

    //author service
    AuthorDto createAuthor(AuthorDto authorDto);

    AuthorDto deleteAuthor(Long id);

    Author getAuthor(Long id);

    AuthorDto getAuthorUpdateDto(Long id);

    AuthorDto updateAuthor(AuthorDto authorDto);

    Page<AuthorResponseCreateDto> getAllAuthors(PageRequestObject pageRequestObject);

    List<AuthorResponseDeleteDto> getAllAuthorDtoList();

    Page<AuthorResponseCreateDto> searchAuthorLikeText(String text, PageRequestObject pageRequestObject);

    Page<AuthorResponseGetDto> getAuthorByFirstLetterSurname(String letter, PageRequestObject pageRequestObject);

    //book service
    BookDto createBook(BookRequestCreateDto bookCreateDto);

    BookDto deleteBook(Long id);

    ItemDto deleteBookFromItem(Long idItem, Long idBook);

    BookDto getBookUpdateDto(Long id);

    BookDto updateBook(BookDto bookDto);

    Page<BookResponseGetDto> getBookByAuthor(Long id, PageRequestObject pageRequestObject);

    Page<BookResponseGetDto> getAllBooks(PageRequestObject pageRequestObject);

    Page<BookResponseGetDto> searchBookLikeText(String text, PageRequestObject pageRequestObject);

    Page<BookResponseGetDto> searchBookByAuthorOrNameLikeText(String text, PageRequestObject pageRequestObject);

    Page<BookResponseGetDto> getBookByItem(Long id, PageRequestObject pageRequestObject);

    Page<BookResponseGetDto> getBookByGenre(String genre, PageRequestObject pageRequestObject);

    //item service
    ItemDto createItem();

    ItemDto deleteItem(Long id);

    ItemDto getItemDto(Long id);

    ItemDto changeItemState(Long id, String state);

    Page<ItemResponseGetDto> getItemByState(String state, PageRequestObject pageRequestObject);

    Page<ItemResponseGetDto> getItemByUser(Long id, PageRequestObject pageRequestObject);

    Page<ItemResponseGetDto> getItemByStateAndUser(String state, Long id, PageRequestObject pageRequestObject);

    //user service
    UserDto getCurrentUserDto();

    UserDto getUserUpdateDtoForAdmin(Long id);

    UserDto getUserUpdateDtoForClient();

    UserDto createUser(UserRegistrationDto userDto);

    UserDto createUser(UserRequestCreateDto userDto);

    UserDto updateUser(UserRequestUpdateAdminDto userDto);

    UserDto updateUser(UserRequestUpdateClientDto userDto);

    UserDto deleteUser(Long id);

    UserDto getUserDto(Long id);

    void changeUserPassword(Password password);

    Page<UserResponseGetDto> getAllUsers(PageRequestObject pageRequestObject);

    //user cart
    void createEmptyCart();

    void clearCart();

    void addBookToCart(Long id);

    void getCart(Model model);
}
