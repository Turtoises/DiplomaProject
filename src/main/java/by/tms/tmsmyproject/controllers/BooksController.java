package by.tms.tmsmyproject.controllers;

import by.tms.tmsmyproject.dto.PageRequestObject;
import by.tms.tmsmyproject.dto.author.AuthorResponseDeleteDto;
import by.tms.tmsmyproject.dto.book.BookRequestCreateDto;
import by.tms.tmsmyproject.dto.book.BookRequestUpdateDto;
import by.tms.tmsmyproject.dto.book.BookResponseGetDto;
import by.tms.tmsmyproject.entities.Book;
import by.tms.tmsmyproject.enums.GenreBook;
import by.tms.tmsmyproject.facade.LibraryFacade;
import by.tms.tmsmyproject.utils.constants.PageRequestObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/books")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class BooksController {

    private final LibraryFacade libraryFacade;

    private final HttpSession session;

    private static int currentPage = 1;
    private static String sizeSortFieldSortDirAsUri = "";
    private static String pageReturn;
    private static String searchText;

    @ModelAttribute("path")
    public String getPathToPage() {
        return "/books/page";
    }

    @ModelAttribute("uri")
    public String getUri() {
        return "/books";
    }

    @ModelAttribute("allGenres")
    public List<String> allGenres() {
        return GenreBook.GENRES;
    }

    @ModelAttribute("allAuthors")
    public List<AuthorResponseDeleteDto> allAuthors() {
        return libraryFacade.getAllAuthorDtoList();
    }

    @GetMapping
    public String getAllBooks() {
        return "redirect:/books/page/1";
    }

    @GetMapping("/page/{pageNumber}")
    public String getAllPage(PageRequestObject pageRequestObject,
                             Model model) {
        currentPage = pageRequestObject.getPageNumber();
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<BookResponseGetDto> page = libraryFacade.getAllBooks(pageRequestObject);
        if (page.getTotalPages() < currentPage) {
            return "redirect:/books/page/" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        model.addAttribute("list", page);
        return "books/all-books-page";
    }

    @GetMapping("/new")
    public String createBookPage(Model model,
                                 @RequestParam(name = "id", required = false) Long id,
                                 @RequestParam(name = "path", required = false) String path) {
        Book book = new Book();
        if (id != null) {
            book.setAuthor(libraryFacade.getAuthor(id));
            pageReturn = path;
        }
        model.addAttribute("object", book);
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("object") @Valid BookRequestCreateDto bookCreateDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        libraryFacade.createBook(bookCreateDto);
        if (pageReturn != null) {
            String path = pageReturn;
            pageReturn = null;
            return "redirect:" + path;
        }
        return "redirect:/books/page/" + currentPage + sizeSortFieldSortDirAsUri;
    }

    @GetMapping("/{id}/edit")
    public String editPage(Model model, @PathVariable("id") Long id,
                           @RequestParam(name = "path", required = false) String path,
                           HttpServletRequest request) {
        model.addAttribute("object", libraryFacade.getBookUpdateDto(id));
        if (path != null) {
            pageReturn = path;
        }
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("search")) {
            pageReturn = referer;
        }
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("object") @Valid BookRequestUpdateDto bookUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        libraryFacade.updateBook(bookUpdateDto);
        if (pageReturn != null) {
            String path = pageReturn;
            pageReturn = null;
            return "redirect:" + path;
        }
        return "redirect:/books/page/" + currentPage + sizeSortFieldSortDirAsUri;
    }

    @GetMapping("/search/page/{pageNumber}")
    public String searchBook(@RequestParam(name = "text", required = false) String text,
                             PageRequestObject pageRequestObject,
                             Model model) {
        currentPage = pageRequestObject.getPageNumber();
        searchText = text == null ? searchText : text;
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<BookResponseGetDto> page = libraryFacade.searchBookLikeText(searchText,pageRequestObject);
        if (page.getTotalPages() < currentPage) {
            return "redirect:/books/search/page/" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        String path = "/books/search/page";
        model.addAttribute("search", "The result of your search: '" + searchText + "'")
                .addAttribute("path", path)
                .addAttribute("genre", "all")
                .addAttribute("list", page);
        return "books/all-books-page";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        libraryFacade.deleteBook(id);
        return "redirect:/books/page/" + currentPage + sizeSortFieldSortDirAsUri;
    }
}
