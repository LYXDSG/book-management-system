package org.example.demo08.common;

import java.util.List;

//第二种Result，有几种结果就有几个result类
public class PageResult<T> {

    private long total;
    //总条数，由COUNT(*)查出来
    private int page;
    //当前页码，从1开始
    private int size;
    //每页条数
    private List<T> list;

    public  PageResult(){}
    //空参构造，有些工具会用到，一般都要写


    public PageResult(long total, int page, int size, List<T> list) {
        this.total = total;
        this.page = page;
        this.size = size;
        this.list = list;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
