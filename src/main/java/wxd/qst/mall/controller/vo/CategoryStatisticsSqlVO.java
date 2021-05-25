package wxd.qst.mall.controller.vo;

import java.io.Serializable;

public class CategoryStatisticsSqlVO implements Serializable {

    private Long categoryId;

    private String categoryName;

    private Integer price;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
