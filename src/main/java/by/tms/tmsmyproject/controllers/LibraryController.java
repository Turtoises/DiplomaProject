package by.tms.tmsmyproject.controllers;

import by.tms.tmsmyproject.dto.PageRequestObject;
import by.tms.tmsmyproject.dto.author.AuthorResponseGetDto;
import by.tms.tmsmyproject.dto.book.BookResponseGetDto;
import by.tms.tmsmyproject.dto.user.UserRegistrationDto;
import by.tms.tmsmyproject.enums.GenreBook;
import by.tms.tmsmyproject.enums.RoleUser;
import by.tms.tmsmyproject.facade.LibraryFacade;
import by.tms.tmsmyproject.utils.constants.ConstantsRegex;
import by.tms.tmsmyproject.utils.constants.PageRequestObjectUtils;
import by.tms.tmsmyproject.utils.currentuser.CurrentUserUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@AllArgsConstructor
/*@SessionAttributes(names = {"cart", "itemsId"})*/
@RequestMapping("/library")
@Slf4j
public class LibraryController {

    private final LibraryFacade libraryFacade;

    private static String sizeSortFieldSortDirAsUri = "";
    private static String currentGenre;
    private static String currentLetter;
    private static String searchText;
    private static String pageForReturn;

    @ModelAttribute("uri")
    public String getUri() {
        return "/library";
    }

    @ModelAttribute("uri_client")
    public String getClientUri() {
        return "/client";
    }

    @ModelAttribute("path")
    public String getPathToPage() {
        return "/library/books/page";
    }

    @ModelAttribute("alphabet")
    public String[] getAlphabet() {
        return ConstantsRegex.ALPHABET.split("");
    }

    @GetMapping
    public String libraryPage(Model model) {
        model.addAttribute("genres", GenreBook.GENRES_LIBRARY);
        return "library/library";
    }

    @GetMapping("/login")
    public String login() {
        log.debug("user:{} login", CurrentUserUtils.getLogin());
        libraryFacade.createEmptyCart();
        return "redirect:/library";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("object") UserRegistrationDto userDto) {
        userDto.setRole(RoleUser.ROLE_USER.name());
        return "library/registration";
    }

    @PostMapping("/registration")
    public String create(@ModelAttribute("object") @Valid UserRegistrationDto userDto, BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "library/registration";
        }
        libraryFacade.createUser(userDto);
        model.addAttribute("registration", "success");
        return "index";
    }

    @GetMapping("/search/page/{pageNumber}")
    public String searchOnPage(@RequestParam(name = "text", required = false) String text,
                               PageRequestObject pageRequestObject,
                               Model model) {
        if (pageRequestObject.getSortField().equals("author_name")) {
            pageRequestObject.setSortField("author.name");
        } else if (pageRequestObject.getSortField().equals("author_surname")) {
            pageRequestObject.setSortField("author.surname");
        }
        searchText = text == null ? searchText : text;
        System.out.println(searchText);
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<BookResponseGetDto> page = libraryFacade.searchBookByAuthorOrNameLikeText(searchText, pageRequestObject);
        if (page.getTotalPages() < pageRequestObject.getPageNumber()) {
            return "redirect:/library/search/page/" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        String path = "/library/search/page";
        model.addAttribute("search", "The result of your search: '" + searchText + "'")
                .addAttribute("path", path)
                .addAttribute("genre", "all")
                .addAttribute("list", page);
        return "library/books-search-page";
    }

    @GetMapping("/books/page/{pageNumber}")
    public String getBooksByGenre(PageRequestObject pageRequestObject,
                                  Model model,
                                  @RequestParam(name = "genre", defaultValue = "null") String genre) {
        currentGenre = genre.equals("null") ? currentGenre : genre;
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<BookResponseGetDto> page = libraryFacade.getBookByGenre(currentGenre, pageRequestObject);
        if (page.getTotalPages() < pageRequestObject.getPageNumber()) {
            return "redirect:/library/books/page/" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        model.addAttribute("genre", currentGenre)
                .addAttribute("list", page);
        return "library/books-by-genre-page";
    }

    @GetMapping("/authors/page/{pageNumber}")
    public String getAuthorsByFirstLetterSurname(PageRequestObject pageRequestObject,
                                                 Model model,
                                                 @RequestParam(name = "letter", defaultValue = "null") String letter) {
        currentLetter = letter.equals("null") ? currentLetter : letter;
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<AuthorResponseGetDto> page = libraryFacade.getAuthorByFirstLetterSurname(currentLetter, pageRequestObject);
        if (page.getTotalPages() < pageRequestObject.getPageNumber()) {
            return "redirect:/library/authors/page/" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        String path = "/library/authors/page";
        model.addAttribute("letter", currentLetter)
                .addAttribute("path", path)
                .addAttribute("list", page);
        return "library/authors-by-letter-page";
    }

    @GetMapping("/authors/{id}/books/page/{pageNumber}")
    public String getBooksThisAuthor(PageRequestObject pageRequestObject,
                                     @PathVariable("id") Long id,
                                     Model model, HttpServletRequest request) {
        pageForReturn = pageForReturn == null ? request.getHeader("Referer") : pageForReturn;
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<BookResponseGetDto> page = libraryFacade.getBookByAuthor(id, pageRequestObject);
        if (page.getTotalPages() < pageRequestObject.getPageNumber()) {
            return "redirect:/library/authors/" + id + "/books/page" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        String path = "/library/authors/" + id + "/books/page";
        model.addAttribute("letter", currentLetter)
                .addAttribute("path", path)
                .addAttribute("list", page)
                .addAttribute("author", libraryFacade.getAuthorUpdateDto(id));
        return "library/books-this-author";
    }

    @GetMapping("/authors/return")
    public String returnToAuthorsByFirstLetter() {
        String referer = pageForReturn;
        pageForReturn = null;
        return "redirect:" + referer;
    }
}

