package by.tms.tmsmyproject.facade;

import by.tms.tmsmyproject.dto.PageRequestObject;
import by.tms.tmsmyproject.dto.Password;
import by.tms.tmsmyproject.dto.author.*;
import by.tms.tmsmyproject.dto.book.BookDto;
import by.tms.tmsmyproject.dto.book.BookRequestCreateDto;
import by.tms.tmsmyproject.dto.book.BookRequestUpdateDto;
import by.tms.tmsmyproject.dto.book.BookResponseGetDto;
import by.tms.tmsmyproject.dto.item.ItemDto;
import by.tms.tmsmyproject.dto.item.ItemResponseGetDto;
import by.tms.tmsmyproject.dto.user.*;
import by.tms.tmsmyproject.entities.Author;
import by.tms.tmsmyproject.entities.Book;
import by.tms.tmsmyproject.entities.Item;
import by.tms.tmsmyproject.entities.User;
import by.tms.tmsmyproject.mapers.AuthorMapper;
import by.tms.tmsmyproject.mapers.BookMapper;
import by.tms.tmsmyproject.mapers.ItemMapper;
import by.tms.tmsmyproject.mapers.UserMapper;
import by.tms.tmsmyproject.services.AuthorService;
import by.tms.tmsmyproject.services.BookService;
import by.tms.tmsmyproject.services.ItemService;
import by.tms.tmsmyproject.services.UserService;
import by.tms.tmsmyproject.utils.CartUtils;
import by.tms.tmsmyproject.utils.constants.PageRequestObjectUtils;
import by.tms.tmsmyproject.utils.currentuser.CurrentUserUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class LibraryFacadeImpl implements LibraryFacade {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    private final BookService bookService;
    private final BookMapper bookMapper;

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    private final UserService userService;
    private final UserMapper userMapper;

    private final HttpSession session;

    private static final String EMPTY_PASSWORD = "";

    //author service
    @Override
    public AuthorDto createAuthor(AuthorDto authorDto) {
        Author author = authorMapper.toEntity((AuthorRequestCreateDto) authorDto);
        return authorMapper.toDtoCreate(authorService.create(author));
    }

    @Override
    public AuthorDto deleteAuthor(Long id) {
        return authorMapper.toDtoDelete(authorService.deleteById(id));
    }

    @Override
    public Author getAuthor(Long id) {
        return authorService.getById(id);
    }

    @Override
    public AuthorDto getAuthorUpdateDto(Long id) {
        return authorMapper.toDtoUpdate(authorService.getById(id));
    }

    @Override
    public AuthorDto updateAuthor(AuthorDto authorDto) {
        Author author = authorMapper.toEntity((AuthorRequestUpdateDto) authorDto);
        return authorMapper.toDtoCreate(authorService.update(author));
    }

    @Override
    public Page<AuthorResponseCreateDto> getAllAuthors(PageRequestObject pageRequestObject) {
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return authorService.getAllPaginated(pageable).map(authorMapper::toDtoCreate);
    }

    @Override
    public List<AuthorResponseDeleteDto> getAllAuthorDtoList() {
        return authorMapper.toDtoList(authorService.getAll());
    }

    @Override
    public Page<AuthorResponseCreateDto> searchAuthorLikeText(String text, PageRequestObject pageRequestObject) {
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return authorService.searchLikeText(text, pageable).map(authorMapper::toDtoCreate);
    }

    @Override
    public Page<AuthorResponseGetDto> getAuthorByFirstLetterSurname(String letter, PageRequestObject pageRequestObject) {
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return authorService.findAuthorByFirstLetterSurnameOrAll(letter, pageable).map(authorMapper::toDtoGet);
    }

    //book service
    @Override
    public BookDto createBook(BookRequestCreateDto bookCreateDto) {
        Book book = bookMapper.toEntity(bookCreateDto);
        return bookMapper.toDtoCreate(bookService.create(book));
    }

    @Override
    public BookDto deleteBook(Long id) {
        return bookMapper.toDtoDelete(bookService.deleteById(id));
    }

    @Override
    public ItemDto deleteBookFromItem(Long idItem, Long idBook) {
        return itemMapper.toGetDto(bookService.deleteBookFromItem(idItem, idBook));
    }

    @Override
    public BookDto getBookUpdateDto(Long id) {
        return bookMapper.toDtoUpdate(bookService.getById(id));
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Book book = bookMapper.toEntity((BookRequestUpdateDto) bookDto);
        return bookMapper.toDtoCreate(bookService.update(book));
    }

    public Page<BookResponseGetDto> getBookByAuthor(Long id, PageRequestObject pageRequestObject) {
        Author author = authorService.getById(id);
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return bookService.findBookByAuthor(author, pageable).map(bookMapper::toDtoCreate);
    }

    @Override
    public Page<BookResponseGetDto> getAllBooks(PageRequestObject pageRequestObject) {
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return bookService.getAllPaginated(pageable).map(bookMapper::toDtoCreate);
    }

    @Override
    public Page<BookResponseGetDto> searchBookLikeText(String text, PageRequestObject pageRequestObject) {
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return bookService.searchBookByNameLikeText(text, pageable).map(bookMapper::toDtoCreate);
    }

    @Override
    public Page<BookResponseGetDto> searchBookByAuthorOrNameLikeText(String text, PageRequestObject pageRequestObject) {
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return bookService.searchBookByAuthorOrNameLikeText(text, pageable).map(bookMapper::toDtoCreate);
    }

    @Override
    public Page<BookResponseGetDto> getBookByItem(Long id, PageRequestObject pageRequestObject) {
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return bookService.findBookByItem(id, pageable).map(bookMapper::toDtoCreate);
    }

    @Override
    public Page<BookResponseGetDto> getBookByGenre(String genre, PageRequestObject pageRequestObject) {
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return bookService.findBookByGenreOrAll(genre, pageable).map(bookMapper::toDtoCreate);
    }

    //item service
    @Override
    public ItemDto createItem() {
        Item item = new Item();
        item.setBooks(bookMapper.toEntityList((ArrayList<BookResponseGetDto>) session.getAttribute("cart")));
        createEmptyCart();
        return itemMapper.toGetDto(itemService.create(item));
    }

    @Override
    public ItemDto deleteItem(Long id) {
        return itemMapper.toDeleteDto(itemService.deleteById(id));
    }

    @Override
    public ItemDto getItemDto(Long id) {
        return itemMapper.toGetDto(itemService.getById(id));
    }

    @Override
    public ItemDto changeItemState(Long id, String state) {
        return itemMapper.toGetDto(itemService.changeState(id, state));
    }

    @Override
    public Page<ItemResponseGetDto> getItemByState(String state, PageRequestObject pageRequestObject) {
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return itemService.findByState(state, pageable).map(itemMapper::toGetDto);
    }

    @Override
    public Page<ItemResponseGetDto> getItemByUser(Long id, PageRequestObject pageRequestObject) {
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return itemService.findByUserId(id, pageable).map(itemMapper::toGetDto);
    }

    @Override
    public Page<ItemResponseGetDto> getItemByStateAndUser(String state, Long id, PageRequestObject pageRequestObject) {
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return itemService.findByStateAndUserId(state, id, pageable).map(itemMapper::toGetDto);
    }

    //user service
    @Override
    public UserDto getCurrentUserDto() {
        return userMapper.toDto(CurrentUserUtils.getUser());
    }

    @Override
    public UserDto getUserUpdateDtoForAdmin(Long id) {
        UserRequestUpdateAdminDto userRequestUpdateAdminDto = userMapper.toAdminUpdateDto(userService.getById(id));
        userRequestUpdateAdminDto.setPassword(EMPTY_PASSWORD);
        return userRequestUpdateAdminDto;
    }

    @Override
    public UserDto getUserUpdateDtoForClient() {
        return userMapper.toClientUpdateDto(CurrentUserUtils.getUser());
    }

    @Override
    public UserDto createUser(UserRegistrationDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userService.create(user));
    }

    @Override
    public UserDto createUser(UserRequestCreateDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userService.create(user));
    }

    @Override
    public UserDto updateUser(UserRequestUpdateAdminDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userService.update(user));
    }

    @Override
    public UserDto updateUser(UserRequestUpdateClientDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userService.update(user));
    }

    @Override
    public UserDto deleteUser(Long id) {
        return userMapper.toDto(userService.deleteById(id));
    }

    @Override
    public UserDto getUserDto(Long id) {
        return userMapper.toDto(userService.getById(id));
    }

    @Override
    public void changeUserPassword(Password password) {
        userService.changePassword(password.getNewPassword());
    }

    @Override
    public Page<UserResponseGetDto> getAllUsers(PageRequestObject pageRequestObject) {
        Pageable pageable = PageRequestObjectUtils.getPageRequest(pageRequestObject);
        return userService.getAllPaginated(pageable).map(userMapper::toDto);
    }

    //user cart
    @Override
    public void createEmptyCart() {
        session.setAttribute("cart", new ArrayList<BookResponseGetDto>());
        session.setAttribute("itemsId", new ArrayList<Long>());
    }

    @Override
    public void clearCart() {
        session.setAttribute("cart", new ArrayList<BookResponseGetDto>());
        session.setAttribute("itemsId", new ArrayList<Long>());
    }

    @Override
    public void addBookToCart(Long id) {
        if (bookService.changeAmountDownward(id)) {
            ArrayList<BookResponseGetDto> cart = (ArrayList<BookResponseGetDto>) session.getAttribute("cart");
            ArrayList<Long> itemsId = (ArrayList<Long>) session.getAttribute("itemsId");
            cart.add(bookMapper.toDtoCreate(bookService.getById(id)));
            itemsId.add(id);
            session.setAttribute("cart", cart);
            session.setAttribute("itemsId", itemsId);
            log.debug("user:{} add book with id:{} into cart", CurrentUserUtils.getLogin(), id);
        }
    }

    @Override
    public void getCart(Model model){
        model.addAttribute("list", session.getAttribute("cart"));
        model.addAttribute("sum", CartUtils.totalAmount((ArrayList<BookResponseGetDto>) session.getAttribute("cart")));
    }

    public void deleteBookFromCart(Long id){

    }

}
