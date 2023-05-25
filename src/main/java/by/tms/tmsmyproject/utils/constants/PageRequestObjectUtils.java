package by.tms.tmsmyproject.utils.constants;

import by.tms.tmsmyproject.dto.PageRequestObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;

public final class PageRequestObjectUtils {

    private PageRequestObjectUtils() {
    }

    public static String getSizeSortFieldSortDirAsUri(Model model, Integer id, String sortField, String sortDir, Integer size) {

        model.addAttribute("size", size)
                .addAttribute("sortField", sortField)
                .addAttribute("sortDir", sortDir)
                .addAttribute("reverseSortDir", sortDir.equals("ASC") ? "DESC" : "ASC")
                .addAttribute("currentPage", id);

        return String.format("?sortField=%s&sortDir=%s&size=%s", sortField, sortDir, size);
    }

    public static String getPageParamForUri(Model model, PageRequestObject pageRequestObject) {
        Integer pageNumber = pageRequestObject.getPageNumber();
        Integer size = pageRequestObject.getSize();
        String sortField = pageRequestObject.getSortField();
        String sortDir = pageRequestObject.getSortDir();

        model.addAttribute("size", size)
                .addAttribute("sortField", sortField)
                .addAttribute("sortDir", sortDir)
                .addAttribute("reverseSortDir", sortDir.equals("ASC") ? "DESC" : "ASC")
                .addAttribute("currentPage", pageNumber);
        return String.format("?sortField=%s&sortDir=%s&size=%s", sortField, sortDir, size);
    }

    public static PageRequest getPageRequest(PageRequestObject pageRequestObject) {
        Integer pageNumber = pageRequestObject.getPageNumber();
        Integer size = pageRequestObject.getSize();
        String sortField = pageRequestObject.getSortField();
        String sortDir = pageRequestObject.getSortDir();

        return PageRequest.of(Math.max(pageNumber - 1, 0), size, Sort.Direction.valueOf(sortDir), sortField);
    }
}
