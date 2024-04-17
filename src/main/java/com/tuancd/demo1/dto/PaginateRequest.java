package com.tuancd.demo1.dto;

import lombok.Data;

@Data
public class PaginateRequest {
    private int page;
    private int size;

    public PaginateRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }
}
