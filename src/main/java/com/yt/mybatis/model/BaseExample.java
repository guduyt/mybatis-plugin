package com.yt.mybatis.model;


import com.yt.commons.Page;

/**
 * BaseExample
 *
 * @author yitao
 * @version 1.0.0
 * @date 2016/8/18 17:19
 */
public abstract class BaseExample {



    protected Page page;

    protected  int limit;

    protected int offset;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
        if(null!=this.page){
            this.limit=this.page.getPageSize();
            this.offset=this.page.getStartRow();
        }
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
