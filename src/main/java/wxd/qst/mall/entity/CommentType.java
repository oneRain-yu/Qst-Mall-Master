package wxd.qst.mall.entity;

/**
 * 评价类型
 */
public enum CommentType {

    MALL_USER_COMMENT("会员评价"), ADMIN_COMMENT("商家回复");


    private String message;

    CommentType() {
    }

    CommentType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
