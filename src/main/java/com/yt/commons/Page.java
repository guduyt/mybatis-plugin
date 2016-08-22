package com.yt.commons;

import com.yt.mybatis.model.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Page
 *
 * @author yitao
 * @version 1.0.0
 * @date 2016/8/18 17:31
 */
public class Page implements Serializable {

    //页大小
    private int pageSize;

    //当前页
    private int currentPage;

    //总页数
    private int totalPage;

    //起始行
    private int startRow;

    //结束行
    private int endRow;

    // 总数
    private int totalRow;

    private List<? extends BaseModel> list;

    public Page(){
        this.pageSize=10;
        this.currentPage=1;
        this.totalRow=0;
    }

    public Page(int pageSize,int currentPage){
        this.pageSize=10;
        this.currentPage=1;
        this.totalRow=0;
        this.pageSize=pageSize;
        this.currentPage=currentPage;
    }

    public Page(int pageSize,int currentPage,int totalRow){
        this.pageSize=10;
        this.currentPage=1;
        this.totalRow=0;
        this.pageSize=pageSize;
        this.currentPage=currentPage;
        this.totalRow=totalRow;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
       this.totalPage= (this.getTotalRow()%this.pageSize)==0?((int)Math.floor(this.getTotalRow()/this.pageSize)):((int)Math.floor(this.getTotalRow()/this.pageSize)+1);
        return this.totalPage==0?1:this.totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getStartRow() {
        return this.currentPage>0?(this.currentPage-1)*this.pageSize:0;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return this.currentPage>0?(this.currentPage)*this.pageSize:this.pageSize;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public List<? extends BaseModel> getList() {
        return list;
    }

    public void setList(List<? extends BaseModel> list) {
        this.list = list;
    }

}
