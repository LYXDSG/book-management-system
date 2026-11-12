package org.example.demo08.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springdoc.core.converters.models.Sort;

@Data

public class BookQuery {

    @Min(value=1,message="页码从一开始")
    private Integer page =1;
    //页码,这个页码不是总页码，总页码无上限，这个页码是要查看的页码

    @Min(value=1,message="每页最少一条")
    @Max(value=50,message="每页最多五十条")
    private Integer size=10;
    //单页搜索条数

    private String keyword;
    //搜索关键字

    private String sortBy="id";
    private String order="desc";
    //排序参数

    //白名单：排序字段只能用${}拼进SQL，所以必须自己档
    public String safeSortBy(){
        String s =sortBy==null?"":sortBy;
        return switch(s){
            case "id","price","stock","title"->s;
            default ->"id";
        };
    }
//- `case "id","price","stock","title"`：**多常量合并 case**
//含义：当 `s` 等于 `"id"` 或者 `"price"` 或者 `"stock"` 或者 `"title"` 任意一个的时候，
//`->s`，直接返回 s 本身,也就是直接返回"id","price"或者"stock"。
//- `default -> "id"`：其他所有情况（包括 s 是空字符串、或者传了别的值比如 `delete`、`drop`），
//统一返回默认值 `"id"和直接输入id效果一致。

    public String safeOrder(){
        return "asc".equals(order)?"asc":"desc";
    }
    public int offset(){
        return (page-1)*size;
    }
    //计算SQL的LIMIT偏移量，偏移量=（页数-1）*每页条数

    public String safeKeyWord(){
        return keyword ==null?"":keyword.trim();
    }
    //吧keyword归一化：null->空串，并去掉首位空格（" java "和"java"应该搜出来的结果相同）

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
