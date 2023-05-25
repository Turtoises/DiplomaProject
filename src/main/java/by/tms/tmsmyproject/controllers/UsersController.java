package by.tms.tmsmyproject.controllers;

import by.tms.tmsmyproject.dto.PageRequestObject;
import by.tms.tmsmyproject.dto.user.UserRequestCreateDto;
import by.tms.tmsmyproject.dto.user.UserRequestUpdateAdminDto;
import by.tms.tmsmyproject.dto.user.UserResponseGetDto;
import by.tms.tmsmyproject.enums.RoleUser;
import by.tms.tmsmyproject.facade.LibraryFacade;
import by.tms.tmsmyproject.utils.constants.PageRequestObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UsersController {

    private final LibraryFacade libraryFacade;

    private static int currentPage = 1;
    private static String sizeSortFieldSortDirAsUri = "";

    @ModelAttribute("path")
    public String getPathToPage() {
        return "/users/page";
    }

    @ModelAttribute("uri")
    public String getUri() {
        return "/users";
    }

    @ModelAttribute("allRoles")
    public List<String> allRoles() {
        return RoleUser.ROLES;
    }

    @GetMapping
    public String getAllUsers() {
        return "redirect:/users/page/1";
    }

    @GetMapping("/page/{pageNumber}")
    public String getAllPage(PageRequestObject pageRequestObject,
                             Model model) {
        currentPage = pageRequestObject.getPageNumber();
        sizeSortFieldSortDirAsUri = PageRequestObjectUtils.getPageParamForUri(model, pageRequestObject);
        Page<UserResponseGetDto> page = libraryFacade.getAllUsers(pageRequestObject);
        if (page.getTotalPages() < currentPage) {
            return "redirect:/users/page/" + page.getTotalPages() + sizeSortFieldSortDirAsUri;
        }
        model.addAttribute("list", page);
        return "users/all-users-page";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("object") UserRequestCreateDto userDto) {
        return "users/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("object") @Valid UserRequestCreateDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/new";
        }
        libraryFacade.createUser(userDto);
        return "redirect:/users/page/" + currentPage + sizeSortFieldSortDirAsUri;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        model.addAttribute("object", libraryFacade.getUserUpdateDtoForAdmin(id));
        return "users/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("object") @Valid UserRequestUpdateAdminDto userDto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        libraryFacade.updateUser(userDto);
        return "redirect:/users/page/" + currentPage + sizeSortFieldSortDirAsUri;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        libraryFacade.deleteUser(id);
        return "redirect:/users/page/" + currentPage + sizeSortFieldSortDirAsUri;
    }
}
