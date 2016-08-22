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

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    protected Page page;

}
