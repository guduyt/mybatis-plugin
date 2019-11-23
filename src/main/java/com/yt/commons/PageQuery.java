package com.yt.commons;

import java.io.Serializable;

/**
 * PageQuery
 *
 * @author yitao
 * @version 1.0.0
 * @date 2019/11/23 11:04
 */
public class PageQuery<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int currentPage = 1;

    private int pageSize = 10;

    private String sort;

    private String order;

    private T query;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public T getQuery() {
        return query;
    }

    public void setQuery(T query) {
        this.query = query;
    }
}
