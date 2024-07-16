package net.nonworkspace.demo.model.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PagingVO extends PageRequest {

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param pageNumber zero-based page number, must not be negative.
     * @param pageSize   the size of the page to be returned, must be greater than 0.
     * @param sort       must not be {@literal null}, use {@link Sort#unsorted()} instead.
     */
    public PagingVO(int pageNumber, int pageSize, Sort sort) {
        super(pageNumber, pageSize, sort);
    }
}
