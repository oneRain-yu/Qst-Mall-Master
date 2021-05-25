package wxd.qst.mall.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import wxd.qst.mall.entity.CommentType;
import wxd.qst.mall.entity.GoodsComment;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class QstMallGoodsCommentVO implements Serializable {

    // id
    private Long id;

    //评价人id
    private Long userId;

    //评价人昵称
    private String userName;

    //商品id
    private Long goodsId;

    //父评价id
    private Long parentCommentId;

    //评价类型
    private CommentType commentType;

    //评价内容
    private String content;

    //是否删除
    private Boolean deleted;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    //商家回复
    private List<QstMallGoodsCommentVO> adminComments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public CommentType getCommentType() {
        return commentType;
    }

    public void setCommentType(CommentType commentType) {
        this.commentType = commentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<QstMallGoodsCommentVO> getAdminComments() {
        return adminComments;
    }

    public void setAdminComments(List<QstMallGoodsCommentVO> adminComments) {
        this.adminComments = adminComments;
    }
}
