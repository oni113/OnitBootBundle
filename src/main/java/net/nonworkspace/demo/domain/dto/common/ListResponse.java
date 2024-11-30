package net.nonworkspace.demo.domain.dto.common;

import lombok.Getter;

import java.util.List;

@Getter
public class ListResponse<T> {

    private final List<T> list;

    private final int count;

    public ListResponse(List<T> list) {
        this.list = list;
        this.count = list != null ? list.size() : 0;
    }
}
