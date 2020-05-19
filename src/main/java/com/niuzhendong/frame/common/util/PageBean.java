package com.niuzhendong.frame.common.util;

import lombok.Data;

import java.util.List;

@Data
public class PageBean<T> {

    private int currentPage;
    private int pageSize;
    private int totalRecord;
    private int totalPage;
    private List<T> list;
    private int startPage;
    private int endPage;
    private int fromIndex;
    private int toIndex;

    public PageBean(int currentPage, int pageSize, int totalRecord) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
        fromIndex = (currentPage - 1) * pageSize;
        toIndex = currentPage * pageSize > totalRecord ? totalRecord : currentPage * pageSize;

        if (totalRecord % pageSize == 0) {
            this.totalPage = totalRecord / pageSize;
        } else {
            this.totalPage = totalRecord / pageSize + 1;
        }
        startPage = 1;
        endPage = 5;
        if (totalPage <= 5) {
            endPage = this.totalPage;
        } else {
            startPage = currentPage - 2;
            endPage = currentPage + 2;

            if (startPage < 1) {
                startPage = 1;
                endPage = 5;
            }
            if (endPage > this.totalPage) {
                endPage = totalPage;
                startPage = endPage - 5;
            }
        }
    }
}
