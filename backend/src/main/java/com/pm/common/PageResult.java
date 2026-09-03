package com.pm.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {

    private long total;
    private long size;
    private long current;
    private List<T> records;

    public static <T> PageResult<T> of(Page<?> page, List<T> records) {
        PageResult<T> result = new PageResult<>();
        result.setTotal(page.getTotal());
        result.setSize(page.getSize());
        result.setCurrent(page.getCurrent());
        result.setRecords(records);
        return result;
    }
}
