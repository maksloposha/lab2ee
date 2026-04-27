package org.example.lab2ee.dto;

import java.util.List;

public class PagedResponse<T> {
    private List<T> data;
    private int page;
    private int pageSize;
    private long totalItems;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrev;

    public PagedResponse() {
    }

    public PagedResponse(List<T> data, int page, int pageSize, long totalItems) {
        this.data = data;
        this.page = page;
        this.pageSize = pageSize;
        this.totalItems = totalItems;
        this.totalPages = pageSize > 0 ? (int) Math.ceil((double) totalItems / pageSize) : 0;
        this.hasNext = page < totalPages;
        this.hasPrev = page > 1;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> d) {
        this.data = d;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int p) {
        this.page = p;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int ps) {
        this.pageSize = ps;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long t) {
        this.totalItems = t;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int tp) {
        this.totalPages = tp;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean h) {
        this.hasNext = h;
    }

    public boolean isHasPrev() {
        return hasPrev;
    }

    public void setHasPrev(boolean h) {
        this.hasPrev = h;
    }
}
