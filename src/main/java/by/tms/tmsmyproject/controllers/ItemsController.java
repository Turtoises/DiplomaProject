package by.tms.tmsmyproject.controllers;

import by.tms.tmsmyproject.dto.PageRequestObject;
import by.tms.tmsmyproject.dto.book.BookResponseGetDto;
import by.tms.tmsmyproject.dto.item.ItemResponseGetDto;
import by.tms.tmsmyproject.facade.LibraryFacade;
import by.tms.tmsmyproject.utils.constants.PageRequestObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@AllArgsConstructor
@RequestMapping("/items")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class ItemsController {

    private final LibraryFacade libraryFacade;

    private static String sizeSortFieldSortDirAsUri = "";
    private static String currentState;
    private static String previousPageWhenDelete;

    @ModelAttribute("path")
    public String getPathToPage() {
        return "/items/page";
    }

    @ModelAttribute("uri")
    public String getUri() {
        return "/items";
    }

    @GetMapping("/page/{pageNumber}")
    public String getItemsByState(PageRequestObject pageRequestObject,
                                  @RequestParam(name = "state", defaultValue = "null") String state,
                                  Model model) {
        currentState = state.equals("null") ? currentState : state;
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<ItemResponseGetDto> page = libraryFacade.getItemByState(currentState, pageRequestObject);
        if (page.getTotalPages() < pageRequestObject.getPageNumber()) {
            return "redirect:/items/page/" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        model.addAttribute("list", page);
        return "items/all-items-page";
    }

    @GetMapping("/{id}/books/page/{pageNumber}")
    public String getBooksFromItem(PageRequestObject pageRequestObject,
                                   @PathVariable("id") Long id,
                                   Model model, HttpServletRequest request) {
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<BookResponseGetDto> page = libraryFacade.getBookByItem(id, pageRequestObject);
        if (page.getTotalPages() < pageRequestObject.getPageNumber()) {
            return "redirect:/items/" + id + "/books/page/" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        previousPageWhenDelete = previousPageWhenDelete == null ? request.getHeader("Referer") : previousPageWhenDelete;
        String path = "/items/" + id + "/books/page";
        model.addAttribute("item", libraryFacade.getItemDto(id))
                .addAttribute("path", path)
                .addAttribute("list", page);
        return "items/books-in-this-item";
    }

    @GetMapping("/users/{id}/page/{pageNumber}")
    public String getItemFromUserById(@PathVariable("id") Long id,
                                      @RequestParam(name = "state", defaultValue = "null") String state,
                                      PageRequestObject pageRequestObject,
                                      Model model) {
        currentState = state.equals("null") ? currentState : state;
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<ItemResponseGetDto> page = libraryFacade.getItemByUser(id, pageRequestObject);
        if (page.getTotalPages() < pageRequestObject.getPageNumber()) {
            return "redirect:/items/users/" + id + "/page" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        String path = "/items/users/" + id + "/page";
        model.addAttribute("path", path)
                .addAttribute("list", page)
                .addAttribute("user", libraryFacade.getUserDto(id));
        return "items/items-this-user";
    }

    @GetMapping("/{id}")
    public String changeState(@PathVariable("id") Long id,
                              @RequestParam(name = "state") String state,
                              HttpServletRequest request) {
        libraryFacade.changeItemState(id, state);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @DeleteMapping("/{id}/books/{id_book}")
    public String deleteBookFromItem(@PathVariable("id_book") Long idBook,
                                     @PathVariable("id") Long idItem,
                                     HttpServletRequest request) {
        libraryFacade.deleteBookFromItem(idItem, idBook);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable("id") Long id) {
        libraryFacade.deleteItem(id);
        return "redirect:" + previousPageWhenDelete;
    }
}
