package by.tms.tmsmyproject.dto;

import lombok.Data;

@Data
public class PageRequestObject {

    private Integer pageNumber;
    private Integer size;
    private String sortField;
    private String sortDir;

    private static final Integer SIZE_DEFAULT = 15;
    private static final String SORT_FIELD_DEFAULT = "id";
    private static final String SORT_DIR_DEFAULT = "ASC";

    public PageRequestObject(Integer pageNumber, Integer size, String sortField, String sortDir) {
        this.pageNumber = pageNumber;
        this.size = size != null ? size : 10;
        this.sortField = sortField != null ? sortField : SORT_FIELD_DEFAULT;
        this.sortDir = sortDir != null ? sortDir : SORT_DIR_DEFAULT;
    }
}
