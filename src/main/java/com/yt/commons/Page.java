package com.yt.commons;

import com.yt.mybatis.model.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Page
 *
 * @author yitao
 * @version 1.0.0
 * @date 2016/8/18 17:31
 */
public class Page implements Serializable {
    private static final long serialVersionUID = 1L;

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

    private List<? extends BaseModel> list=new ArrayList<>();

    public Page(){
        this.pageSize=10;
        this.currentPage=1;
        this.totalRow=0;
        init();
    }

    public Page(int pageSize,int currentPage){
        this.pageSize=10;
        this.currentPage=1;
        this.totalRow=0;
        this.pageSize=pageSize;
        this.currentPage=currentPage;
        init();
    }

    public Page(int pageSize,int currentPage,int totalRow){
        this.pageSize=10;
        this.currentPage=1;
        this.totalRow=0;
        this.pageSize=pageSize;
        this.currentPage=currentPage;
        this.totalRow=totalRow;
        init();
    }

    /*计算总页数、开始行、结束行*/
    private void init(){
        this.totalPage= (this.getTotalRow()%this.pageSize)==0?((int)Math.floor(this.getTotalRow()/this.pageSize)):((int)Math.floor(this.getTotalRow()/this.pageSize)+1);
        this.startRow=this.currentPage>0?(this.currentPage-1)*this.pageSize:0;
        this.endRow=this.currentPage>0?(this.currentPage)*this.pageSize:this.pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
          if(pageSize<0){
              pageSize=10;
          }
        this.pageSize = pageSize;
        init();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        if(currentPage<1){
            currentPage=1;
        }

        if(this.totalPage !=0 &&currentPage>=this.totalPage){
            currentPage=this.totalPage;
        }
        this.currentPage = currentPage;
        init();
    }

    public int getTotalPage() {
        return this.totalPage==0?1:this.totalPage;
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
        if(totalRow<0)
            totalRow=this.totalRow;
        this.totalRow = totalRow;
        init();
    }

    public List<? extends BaseModel> getList() {
        return list;
    }

    public void setList(List<? extends BaseModel> list) {
        this.list = list;
    }

}
