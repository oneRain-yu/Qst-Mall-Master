package wxd.qst.mall.controller.vo;

import wxd.qst.mall.entity.AfterSaleStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 订单列表页面VO
 */
public class QstMallOrderListVO implements Serializable {

    private Long orderId;

    private String orderNo;

    private Integer totalPrice;

    private Byte payType;

    private String payTypeString;

    private Byte orderStatus;

    private String orderStatusString;

    private String userAddress;

    private AfterSaleStatus afterSaleStatus;

    private String afterSaleStatusStr;

    private String afterSaleStatusString;

    private String afterSaleTransNo;

    private Date createTime;

    private List<QstMallOrderItemVO> qstMallOrderItemVOS;

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
        this.orderNo = orderNo;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }

    public String getPayTypeString() {
        return payTypeString;
    }

    public void setPayTypeString(String payTypeString) {
        this.payTypeString = payTypeString;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusString() {
        return orderStatusString;
    }

    public void setOrderStatusString(String orderStatusString) {
        this.orderStatusString = orderStatusString;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public AfterSaleStatus getAfterSaleStatus() {
        return afterSaleStatus;
    }

    public String getAfterSaleStatusString() {
        return afterSaleStatusString;
    }

    public void setAfterSaleStatusString(String afterSaleStatusString) {
        this.afterSaleStatusString = afterSaleStatusString;
    }

    public void setAfterSaleStatus(AfterSaleStatus afterSaleStatus) {
        this.afterSaleStatus = afterSaleStatus;
    }

    public String getAfterSaleStatusStr() {
        return afterSaleStatusStr;
    }

    public void setAfterSaleStatusStr(String afterSaleStatusStr) {
        this.afterSaleStatusStr = afterSaleStatusStr;
    }

    public String getAfterSaleTransNo() {
        return afterSaleTransNo;
    }

    public void setAfterSaleTransNo(String afterSaleTransNo) {
        this.afterSaleTransNo = afterSaleTransNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<QstMallOrderItemVO> getQstMallOrderItemVOS() {
        return qstMallOrderItemVOS;
    }

    public void setQstMallOrderItemVOS(List<QstMallOrderItemVO> qstMallOrderItemVOS) {
        this.qstMallOrderItemVOS = qstMallOrderItemVOS;
    }
}
