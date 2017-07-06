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
    private int pageSize;

    // 当前页
    private int currentPage;

    // 总页数
    private int totalPage;

    // 起始行
    private int startRow;

    // 结束行
    private int endRow;

    // 总数
    private int totalRow;

    // 开始行
    private int limit;

    // 查询数量
    private int offset;

    public BaseExample() {
        this.pageSize = 10;
        this.currentPage = 1;
        this.totalRow = 0;
        init();
    }

    /* 计算总页数、开始行、结束行 */
    private void init() {
        this.totalPage = (this.getTotalRow() % this.pageSize) == 0 ? ((int) Math.floor(this.getTotalRow() / this.pageSize))
                : ((int) Math.floor(this.getTotalRow() / this.pageSize) + 1);
        this.startRow = this.currentPage > 0 ? (this.currentPage - 1) * this.pageSize : 0;
        this.endRow = this.currentPage > 0 ? (this.currentPage) * this.pageSize : this.pageSize;
        this.limit = this.startRow;
        this.offset = this.pageSize;
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

        if (this.totalPage != 0 && currentPage >= this.totalPage) {
            currentPage = this.totalPage;
        }
        this.currentPage = currentPage;
        init();
    }

    public int getTotalPage() {
        return this.totalPage == 0 ? 1 : this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
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

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        if (totalRow < 0)
            totalRow = this.totalRow;
        this.totalRow = totalRow;
        init();
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
