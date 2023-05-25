package by.tms.tmsmyproject.controllers;

import by.tms.tmsmyproject.dto.PageRequestObject;
import by.tms.tmsmyproject.dto.book.BookResponseGetDto;
import by.tms.tmsmyproject.entities.Author;
import by.tms.tmsmyproject.dto.author.AuthorRequestCreateDto;
import by.tms.tmsmyproject.dto.author.AuthorRequestUpdateDto;
import by.tms.tmsmyproject.dto.author.AuthorResponseCreateDto;
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
import javax.validation.Valid;

@Controller
@AllArgsConstructor
@RequestMapping("/authors")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AuthorsController {

    private final LibraryFacade libraryFacade;

    private static int currentPage = 1;
    private static String sizeSortFieldSortDirAsUri = "";
    private static String pageForReturn;
    private static String searchText;

    @ModelAttribute("path")
    public String getPathToPage() {
        return "/authors/page";
    }

    @ModelAttribute("uri")
    public String getUri() {
        return "/authors";
    }

    @GetMapping
    public String getAllAuthors() {
        return "redirect:/authors/page/1";
    }

    @GetMapping("/new")
    public String newAuthor(@ModelAttribute("object") Author author) {
        return "authors/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("object") @Valid AuthorRequestCreateDto authorCreateDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "authors/new";
        }
        libraryFacade.createAuthor(authorCreateDto);
        return "redirect:/authors/page/" + currentPage + sizeSortFieldSortDirAsUri;
    }

    @GetMapping("/page/{pageNumber}")
    public String getAllPage(PageRequestObject pageRequestObject,
                             Model model) {
        currentPage = pageRequestObject.getPageNumber();
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<AuthorResponseCreateDto> page = libraryFacade.getAllAuthors(pageRequestObject);
        if (page.getTotalPages() < currentPage) {
            return "redirect:/authors/page/" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        model.addAttribute("list", page);
        return "authors/all-authors-page";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("object", libraryFacade.getAuthorUpdateDto(id));
        return "authors/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("object") @Valid AuthorRequestUpdateDto authorUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "authors/edit";
        }
        libraryFacade.updateAuthor(authorUpdateDto);
        return "redirect:/authors/page/" + currentPage + sizeSortFieldSortDirAsUri;
    }

    @GetMapping("/{id}/books/page/{pageNumber}")
    public String authorBooks(Model model,
                              @PathVariable("id") Long id,
                              PageRequestObject pageRequestObject,
                              HttpServletRequest request) {
        currentPage = pageRequestObject.getPageNumber();
        if (currentPage == 1) {
            pageForReturn = request.getHeader("Referer");
        }
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<BookResponseGetDto> page = libraryFacade.getBookByAuthor(id, pageRequestObject);
        if (page.getTotalPages() < currentPage) {
            return "redirect:/authors/" + id + "/books/page/" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        String path = "/authors/" + id + "/books/page";
        if (pageForReturn != null && pageForReturn.contains("search")) {
            model.addAttribute("search", searchText);
        }
        model.addAttribute("path", path)
                .addAttribute("list", page)
                .addAttribute("author", libraryFacade.getAuthorUpdateDto(id));
        return "authors/author-books";
    }

    @GetMapping("/return")
    public String returnToAllAuthors() {
        return "redirect:" + pageForReturn;
    }

    @GetMapping("/{id}/books/new")
    public String addNewBookThisAuthorPage(@PathVariable("id") Long id,
                                           HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return "redirect:/books/new?path=" + referer + "&id=" + id;
    }

    @GetMapping("/{idAuthor}/books/{id}/edit")
    public String editBookThisAuthorPage(@PathVariable("id") Long id,
                                         @PathVariable("idAuthor") Long idAuthor,
                                         HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return "redirect:/books/" + id + "/edit?path=" + referer;
    }

    @GetMapping("/search/page/{pageNumber}")
    public String searchAuthor(@RequestParam(name = "text", required = false) String text,
                               PageRequestObject pageRequestObject,
                               Model model) {
        searchText = text == null ? searchText : text;
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<AuthorResponseCreateDto> page = libraryFacade.searchAuthorLikeText(searchText, pageRequestObject);
        if (page.getTotalPages() < currentPage) {
            return "redirect:/search/page/" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        String path = "/authors/search/page";
        model.addAttribute("search", "The result of your search: '" + searchText + "'")
                .addAttribute("path", path)
                .addAttribute("list", page);
        return "authors/all-authors-page";
    }

    @GetMapping("/search/return")
    public String returnToSearchResult() {
        String text = searchText;
        searchText = null;
        return "redirect:/authors/search/page/1?text=" + text;
    }

    @DeleteMapping("/{id}")
    public String deleteAuthor(@PathVariable("id") Long id) {
        libraryFacade.deleteAuthor(id);
        return "redirect:/authors/page/" + currentPage + sizeSortFieldSortDirAsUri;
    }

    @DeleteMapping("/{idAuthor}/books/{id}")
    public String deleteBookThisAuthor(@PathVariable("id") Long id,
                                       @PathVariable("idAuthor") Long idAuthor,
                                       HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        libraryFacade.deleteBook(id);
        return "redirect:" + referer;
    }
}
