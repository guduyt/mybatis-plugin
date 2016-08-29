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

    /*设置分页信息到limit、offset*/
    public void setPage(Page page) {
        this.page = page;
        if(null!=this.page){
            this.limit=this.page.getPageSize();
            this.offset=this.page.getStartRow();
        }
    }

    /*当Page信息有变动后，调用这个方法重新设置分页信息到limit、offset*/
    public void resetPage(){
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
