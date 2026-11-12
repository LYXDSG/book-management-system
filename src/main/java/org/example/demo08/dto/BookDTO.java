package org.example.demo08.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class BookDTO {
    @NotNull(message="书名不能为空")
    private String title;
    @NotNull(message="作者不能为空")
    private String author;
    @NotNull(message="价格不能为空")
    @DecimalMin(value="0.00",message="价格不能是负数")
    private BigDecimal price;
    @NotNull(message="库存不能为空")
    @Min(value=0,message="库存不能为负数")
    private Integer stock;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
