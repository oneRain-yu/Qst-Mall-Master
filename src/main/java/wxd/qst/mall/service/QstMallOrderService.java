package wxd.qst.mall.service;

import wxd.qst.mall.controller.vo.*;
import wxd.qst.mall.entity.QstMallOrder;
import wxd.qst.mall.util.PageQueryUtil;
import wxd.qst.mall.util.PageResult;
import wxd.qst.mall.util.Result;

import java.util.List;
import java.util.Map;

public interface QstMallOrderService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getQstMallOrdersPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     *
     * @param qstMallOrder
     * @return
     */
    String updateOrderInfo(QstMallOrder qstMallOrder);

    /**
     * 配货
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    /**
     * 保存订单
     *
     * @param user
     * @param mySettleShoppingCartItems
     * @return
     */
    String saveOrder(QstMallUserVO user, List<QstMallShoppingCartItemVO> mySettleShoppingCartItems);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @param userId
     * @return
     */
    QstMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 获取订单详情
     *
     * @param orderNo
     * @return
     */
    QstMallOrder getQstMallOrderByOrderNo(String orderNo);

    /**
     * 我的订单列表
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<QstMallOrderItemVO> getOrderItems(Long id);

    /**
     * 售后申请
     * @param orderId
     * @return
     */
    String afterSaleApply(Long orderId);

    /**
     * 审核售后申请
     * @param orderId
     * @param accept
     * @return
     */
    String acceptAfterSale(Long orderId, Integer accept);

    /**
     * 售后物流保存
     * @param orderId
     * @param afterSaleTransNo
     * @return
     */
    String afterSaleTransNoSave(Long orderId, Long afterSaleTransNo);

    /**
     * 售后确认收货
     * @param orderId
     * @return
     */
    String confirmAfterSaleReceive(Long orderId);

    /**
     * 统计分类销量信息
     * @param params
     * @return
     */
    Result<CategoryStatisticsVO> categoryStatistics(Map<String, Object> params);
}
