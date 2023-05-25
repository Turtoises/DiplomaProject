package by.tms.tmsmyproject.controllers;

import by.tms.tmsmyproject.dto.PageRequestObject;
import by.tms.tmsmyproject.dto.Password;
import by.tms.tmsmyproject.dto.book.BookResponseGetDto;
import by.tms.tmsmyproject.dto.item.ItemResponseGetDto;
import by.tms.tmsmyproject.dto.user.UserRequestUpdateClientDto;
import by.tms.tmsmyproject.enums.StateItem;
import by.tms.tmsmyproject.facade.LibraryFacade;
import by.tms.tmsmyproject.mapers.BookMapper;
import by.tms.tmsmyproject.mapers.ItemMapper;
import by.tms.tmsmyproject.mapers.UserMapper;
import by.tms.tmsmyproject.services.BookService;
import by.tms.tmsmyproject.services.ItemService;
import by.tms.tmsmyproject.services.UserService;
import by.tms.tmsmyproject.utils.CartUtils;
import by.tms.tmsmyproject.utils.constants.PageRequestObjectUtils;
import by.tms.tmsmyproject.utils.currentuser.CurrentUserUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/client")
/*@SessionAttributes(names = {"cart", "itemsId"})*/
@PreAuthorize("hasRole('ROLE_USER')")
@Slf4j
public class ClientController {

    private final LibraryFacade libraryFacade;

    private final UserService userService;
    private final UserMapper userMapper;

    private final BookService bookService;
    private final BookMapper bookMapper;

    private final ItemService itemService;
    private final ItemMapper itemMapper;

    private final PasswordEncoder passwordEncoder;

    private static String sizeSortFieldSortDirAsUri = "";
    private static String currentState;

    @ModelAttribute("uri")
    public String getUri() {
        return "/client";
    }

    @ModelAttribute("path")
    public String getPathToPage() {
        return "/client/item/page";
    }

    /*@ModelAttribute("cart")
    public List<BookResponseGetDto> createCart() {
        return new ArrayList<>();
    }

    @ModelAttribute("itemsId")
    public List<Long> createListIdItemsInCart() {
        return new ArrayList<>();
    }*/

    @GetMapping
    public String clientPage(Model model) {
        model.addAttribute("user", libraryFacade.getCurrentUserDto());
        return "client/client-page";
    }

    @GetMapping("/logout")
    public String logoutClient(@ModelAttribute("cart") List<BookResponseGetDto> cart,
                               @ModelAttribute("itemsId") List<Long> itemsId) {
        clearUserCart(cart, itemsId);
        log.debug("user:{} logout", CurrentUserUtils.getLogin());
        return "redirect:/logout";
    }

    @GetMapping("/edit/data")
    public String changeClientDataPage(Model model) {
        model.addAttribute("object", libraryFacade.getUserUpdateDtoForClient());
        return "client/edit-data";
    }

    @PatchMapping("/edit/data")
    public String changeClientData(@ModelAttribute("object") @Valid UserRequestUpdateClientDto userDto,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "client/edit-data";
        }
        libraryFacade.updateUser(userDto);
        model.addAttribute("message", "The data has been successfully changed");
        return "client/edit-data";
    }

    @GetMapping("/edit/password")
    public String changeClientPasswordPage(@ModelAttribute("object") Password password) {
        return "client/edit-password";
    }

    @PatchMapping("/edit/password")
    public String changeClientPassword(@ModelAttribute("object") @Valid Password password, BindingResult bindingResult,
                                       Model model) {

        if (!bindingResult.hasErrors()) {
            model.addAttribute("message", "The password has been successfully changed");
            libraryFacade.changeUserPassword(password);
        }
        return "client/edit-password";
    }

    @GetMapping("/books/{id}/cart")
    public String addBookToCart(@PathVariable("id") Long id,
                                /*@ModelAttribute("cart") List<BookResponseGetDto> cart,
                                @ModelAttribute("itemsId") List<Long> itemsId,*/
                                HttpServletRequest request) {
        /*if (bookService.changeAmountDownward(id)) {
            cart.add(bookMapper.toDtoCreate(bookService.getById(id)));
            log.debug("user:{} add book with id:{} into cart", CurrentUserUtils.getLogin(), id);
            itemsId.add(id);
        }*/
        libraryFacade.addBookToCart(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @GetMapping("/cart")
    public String getCart(/*@ModelAttribute("cart") List<BookResponseGetDto> cart,*/
                          Model model) {
       /* model.addAttribute("list", cart);
        model.addAttribute("sum", CartUtils.totalAmount(cart));*/
        libraryFacade.getCart(model);
        return "client/cart";
    }

    @DeleteMapping("/cart/{id}")
    public String deleteBookFromCart(@PathVariable("id") Long id,
                                     @ModelAttribute("cart") List<BookResponseGetDto> cart,
                                     @ModelAttribute("itemsId") List<Long> itemsId,
                                     Model model,
                                     HttpServletRequest request) {
        if (!CollectionUtils.isEmpty(cart)) {
            cart.remove(itemsId.indexOf(id));
            itemsId.remove(id);
        }
        bookService.changeAmountUpward(id);
        model.addAttribute("list", cart);
        String referer = request.getHeader("Referer");
        log.debug("user:{} delete book with id:{} into cart", CurrentUserUtils.getLogin(), id);
        return "redirect:" + referer;
    }

    @GetMapping("/cart/item")
    public String formItemFromCart(/*@ModelAttribute("cart") List<BookResponseGetDto> cart,
                                   @ModelAttribute("itemsId") List<Long> itemsId*/) {
        libraryFacade.createItem();
  /*      cart.clear();
        itemsId.clear();*/
        return "redirect:/client/item/page/1?state=CREATE";
    }

    @GetMapping("/cart/clear")
    public String clearCart(@ModelAttribute("cart") List<BookResponseGetDto> cart,
                            @ModelAttribute("itemsId") List<Long> itemsId) {
        clearUserCart(cart, itemsId);
        log.debug("user:{} clear cart", CurrentUserUtils.getLogin());
        return "redirect:/client";
    }

    @GetMapping("/item/page/{pageNumber}")
    public String getItemByState(PageRequestObject pageRequestObject,
                                 Model model,
                                 @RequestParam(name = "state", defaultValue = "null") String state) {
        currentState = state.equals("null") ? currentState : state;
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<ItemResponseGetDto> page = libraryFacade.getItemByStateAndUser(currentState, CurrentUserUtils.getId(), pageRequestObject);
        if (page.getTotalPages() < pageRequestObject.getPageNumber()) {
            return "redirect:/client/item/page/" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        model.addAttribute("list", page);
        return "client/items-by-state";
    }

    @GetMapping("/item/{id}/books/page/{pageNumber}")
    public String getBooksFromItem(@PathVariable("pageNumber") Integer pageNumber,
                                   @PathVariable("id") Long id,
                                   Model model,
                                   @RequestParam(name = "size", defaultValue = "5") Integer size,
                                   @RequestParam(name = "sortField", defaultValue = "name") String sortField,
                                   @RequestParam(name = "sortDir", defaultValue = "ASC") String sortDir) {
        Long currentUserId = CurrentUserUtils.getId();
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, size, Sort.Direction.valueOf(sortDir), sortField);
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getSizeSortFieldSortDirAsUri(model, pageNumber, sortField, sortDir, size);
        Page<BookResponseGetDto> page = bookService.findBookByItemAndUserId(id, currentUserId, pageRequest).map(bookMapper::toDtoCreate);
        if (pageNumber > 1 && (page.getTotalPages() < pageNumber)) {
            return "redirect:/client/item/" + id + "/books/page/" + (pageNumber - 1) + sizeSortFieldSortDirAsUri;
        }
        String path = "/client/item/" + id + "/books/page";
        model.addAttribute("item", itemMapper.toGetDto(itemService.getById(id)))
                .addAttribute("path", path)
                .addAttribute("list", page);
        return "client/books-in-this-item";
    }

    @GetMapping("/item/{id}")
    public String changeState(@PathVariable("id") Long id,
                              @RequestParam(name = "state") String state,
                              HttpServletRequest request) {
        String newState = state.toUpperCase();
        String oldState = itemService.getById(id).getState().toString();
        if (oldState.equals(StateItem.CREATE.toString()) && newState.equals(StateItem.CHANGING.toString())
                || newState.equals(StateItem.CREATE.toString()) && oldState.equals(StateItem.CHANGING.toString())) {
            itemService.changeState(id, state);
        }
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @DeleteMapping("/item/{id}/books/{id_book}")
    public String deleteBookFromItem(@PathVariable("id_book") Long idBook,
                                     @PathVariable("id") Long idItem,
                                     HttpServletRequest request) {
        bookService.deleteBookFromItem(idItem, idBook);
        bookService.changeAmountUpward(idBook);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    private void clearUserCart(List<BookResponseGetDto> cart, List<Long> itemsId) {
        if (!CollectionUtils.isEmpty(cart)) {
            cart.clear();
            itemsId.forEach(bookService::changeAmountUpward);
            itemsId.clear();
        }
    }
}



