package com.yt.mybatis.model;


/**
 * BaseExample
 *
 * @author yitao
 * @version 1.0.0
 * @date 2016/8/18 17:19
 */
public abstract class BaseExample {

    // 页大小
    private int pageSize=10;

    // 当前页
    private int currentPage=1;

    // 起始行
    private int startRow;

    // 结束行
    private int endRow;

    protected  int limit;

    protected int offset;

    private void init() {
        this.startRow = this.currentPage > 0 ? (this.currentPage - 1) * this.pageSize : 0;
        this.endRow = this.currentPage > 0 ? (this.currentPage) * this.pageSize : this.pageSize;
        this.limit = this.pageSize;
        this.offset = this.startRow;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize < 0) {
            pageSize = 10;
        }
        this.pageSize = pageSize;
        init();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if (currentPage < 1) {
            currentPage = 1;
        }
        this.currentPage = currentPage;
        init();
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }


    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

}
