package com.pm.common;

import lombok.Data;

@Data
public class PageQuery {

    private int page = 1;
    private int size = 10;
}
