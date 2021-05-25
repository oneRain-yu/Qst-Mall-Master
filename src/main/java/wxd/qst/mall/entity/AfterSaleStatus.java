package wxd.qst.mall.entity;

/**
 * 售后状态
 */
public enum AfterSaleStatus {

    APPLY("申请售后"), WAIT_RECEIVE("等待退货物流"), FINISH("退货结束"), REFUSED("拒绝退货");


    private String message;

    AfterSaleStatus() {
    }

    AfterSaleStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
