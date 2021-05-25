package wxd.qst.mall.controller.vo;

import java.io.Serializable;
import java.util.List;

public class CategoryStatisticsVO implements Serializable {

    private List<String> categoryNames;

    private List<Integer> prices;

    public List<String> getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(List<String> categoryNames) {
        this.categoryNames = categoryNames;
    }

    public List<Integer> getPrices() {
        return prices;
    }

    public void setPrices(List<Integer> prices) {
        this.prices = prices;
    }
}
