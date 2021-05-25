package wxd.qst.mall.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
/*
* 订单信息
* */
public class QstMallOrder {
    private Long orderId;

    private String orderNo;

    private Long userId;

    private Integer totalPrice;

    //支付状态:0.未支付,1.支付成功,-1:支付失败
    private Byte payStatus;

    //支付方式：0.无 1.支付宝支付 2.微信支付
    private Byte payType;

    private Date payTime;

    //订单状态：0.待支付 1.已支付 2.配货完成 3:出库成功 4.交易成功 -1.手动关闭 -2.超时关闭 -3.商家关闭
    private Byte orderStatus;

    private String extraInfo;

    private String userAddress;

    //售后状态
    private AfterSaleStatus afterSaleStatus;

    //售后物流
    private String afterSaleTransNo;

    private Byte isDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


    public QstMallOrder() {
    }

    public QstMallOrder(Long orderId, String orderNo, Long userId, Integer totalPrice, Byte payStatus, Byte payType, Date payTime, Byte orderStatus, String extraInfo, String userAddress, Byte isDeleted, Date createTime, Date updateTime) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.payStatus = payStatus;
        this.payType = payType;
        this.payTime = payTime;
        this.orderStatus = orderStatus;
        this.extraInfo = extraInfo;
        this.userAddress = userAddress;
        this.isDeleted = isDeleted;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Byte getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public AfterSaleStatus getAfterSaleStatus() {
        return afterSaleStatus;
    }

    public void setAfterSaleStatus(AfterSaleStatus afterSaleStatus) {
        this.afterSaleStatus = afterSaleStatus;
    }

    public String getAfterSaleTransNo() {
        return afterSaleTransNo;
    }

    public void setAfterSaleTransNo(String afterSaleTransNo) {
        this.afterSaleTransNo = afterSaleTransNo;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo == null ? null : extraInfo.trim();
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress == null ? null : userAddress.trim();
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", orderId=").append(orderId);
        sb.append(", orderNo=").append(orderNo);
        sb.append(", userId=").append(userId);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append(", payStatus=").append(payStatus);
        sb.append(", payType=").append(payType);
        sb.append(", payTime=").append(payTime);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", extraInfo=").append(extraInfo);
        sb.append(", userAddress=").append(userAddress);
        sb.append(", isDeleted=").append(isDeleted);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }
}