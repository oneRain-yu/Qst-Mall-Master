package wxd.qst.mall.service.impl;

import wxd.qst.mall.common.*;
import wxd.qst.mall.controller.vo.*;
import wxd.qst.mall.dao.*;
import wxd.qst.mall.entity.*;
import wxd.qst.mall.service.QstMallOrderService;
import wxd.qst.mall.service.QstMallPromotionService;
import wxd.qst.mall.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class QstMallOrderServiceImpl implements QstMallOrderService {

    @Autowired
    private QstMallOrderMapper qstMallOrderMapper;
    @Autowired
    private QstMallOrderItemMapper qstMallOrderItemMapper;
    @Autowired
    private QstMallShoppingCartItemMapper qstMallShoppingCartItemMapper;
    @Autowired
    private QstMallGoodsMapper qstMallGoodsMapper;
    @Autowired
    private QstMallPromotionService qstMallPromotionService;
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public PageResult getQstMallOrdersPage(PageQueryUtil pageUtil) {
        List<QstMallOrder> qstMallOrders = qstMallOrderMapper.findQstMallOrderList(pageUtil);
        int total = qstMallOrderMapper.getTotalQstMallOrders(pageUtil);
        PageResult pageResult = new PageResult(qstMallOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(QstMallOrder qstMallOrder) {
        QstMallOrder temp = qstMallOrderMapper.selectByPrimaryKey(qstMallOrder.getOrderId());
        //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(qstMallOrder.getTotalPrice());
            temp.setUserAddress(qstMallOrder.getUserAddress());
            temp.setUpdateTime(new Date());
            if (qstMallOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<QstMallOrder> orders = qstMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (QstMallOrder qstMallOrder : orders) {
                if (qstMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += qstMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (qstMallOrder.getOrderStatus() != 1) {
                    errorOrderNos += qstMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
                if (qstMallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<QstMallOrder> orders = qstMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (QstMallOrder qstMallOrder : orders) {
                if (qstMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += qstMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (qstMallOrder.getOrderStatus() != 1 && qstMallOrder.getOrderStatus() != 2) {
                    errorOrderNos += qstMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行出库操作 修改订单状态和更新时间
                if (qstMallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行出库操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
                } else {
                    return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //查询所有的订单 判断状态 修改状态和更新时间
        List<QstMallOrder> orders = qstMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (QstMallOrder qstMallOrder : orders) {
                // isDeleted=1 一定为已关闭订单
                if (qstMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += qstMallOrder.getOrderNo() + " ";
                    continue;
                }
                //已关闭或者已完成无法关闭订单
                if (qstMallOrder.getOrderStatus() == 4 || qstMallOrder.getOrderStatus() < 0) {
                    errorOrderNos += qstMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
                if (qstMallOrderMapper.closeOrder(Arrays.asList(ids), QstMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //订单此时不可执行关闭操作
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "订单不能执行关闭操作";
                } else {
                    return "你选择的订单不能执行关闭操作";
                }
            }
        }
        //未查询到数据 返回错误提示
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(QstMallUserVO user, List<QstMallShoppingCartItemVO> mySettleShoppingCartItems) {
        if (CollectionUtils.isEmpty(mySettleShoppingCartItems)){
            return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
        }
        Long userId = user.getUserId();
        String address = user.getAddress();
        //购物车itemIds
        List<Long> cartItemIds = new ArrayList<>();
        //订单总价格
        Integer totalPrice = 0;
        //减库存
        for (QstMallShoppingCartItemVO itemVO : mySettleShoppingCartItems){
            cartItemIds.add(itemVO.getCartItemId());
            Long goodsId = itemVO.getGoodsId();
            Integer goodsCount = itemVO.getGoodsCount();
            StockNumDTO stockNumDTO = new StockNumDTO(goodsId, goodsCount);
            //减少库存
            int updateRows = qstMallGoodsMapper.updateStockNumSingle(stockNumDTO);
            if (updateRows == 0){
                //修改库存失败，事务回滚
                throw new RuntimeException("商品" + itemVO.getGoodsId() + ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
            //单价
            Integer price = queryPriceByGoodsId(goodsId);
            totalPrice += price*goodsCount;
        }
        //保存主订单
        //产生订单号
        String orderNo = NumberUtil.genOrderNo();
        QstMallOrder mallOrder = new QstMallOrder();
        mallOrder.setOrderNo(orderNo);
        mallOrder.setUserId(userId);
        mallOrder.setTotalPrice(totalPrice);
        mallOrder.setPayStatus((byte) 0);
        mallOrder.setPayType((byte) 0);
        mallOrder.setOrderStatus((byte) 0);
        mallOrder.setUserAddress(address);
        mallOrder.setCreateTime(new Date());
        mallOrder.setUpdateTime(new Date());
        mallOrder.setIsDeleted((byte) 0);

        //新增主订单
        int insertOrderNum = qstMallOrderMapper.insertSelective(mallOrder);
        if (insertOrderNum == 0){
            throw new RuntimeException(ServiceResultEnum.ORDER_GENERATE_ERROR.getResult());
        }
        //主订单id
        Long orderId = mallOrder.getOrderId();
        //保存子订单
        List<QstMallOrderItem> orderItems = new ArrayList<>();
        for (QstMallShoppingCartItemVO itemVO : mySettleShoppingCartItems){
            Long goodsId = itemVO.getGoodsId();
            Integer goodsCount = itemVO.getGoodsCount();
            Integer price = queryPriceByGoodsId(goodsId);

            QstMallOrderItem orderItem = new QstMallOrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setGoodsId(goodsId);
            orderItem.setGoodsName(itemVO.getGoodsName());
            orderItem.setGoodsCoverImg(itemVO.getGoodsCoverImg());
            orderItem.setSellingPrice(price);
            orderItem.setGoodsCount(goodsCount);
            orderItem.setCreateTime(new Date());
            orderItems.add(orderItem);
        }
        qstMallOrderItemMapper.insertBatch(orderItems);
        //从购物车移除已支付商品
        qstMallShoppingCartItemMapper.deleteBatch(cartItemIds);
        return orderNo;
    }

    /**
     * 通过商品id，返回促销价或者原价
     * @param goodsId
     * @return
     */
    private Integer queryPriceByGoodsId(Long goodsId) {
        Integer price = 0;
        QstMallGoods qstMallGoods = qstMallGoodsMapper.selectByPrimaryKey(goodsId);
        Promotion goodsPromotion = qstMallPromotionService.getPromotionsByGoodsId(goodsId);
        Date now = new Date();
        if (goodsPromotion != null && goodsPromotion.getStartTime() != null && goodsPromotion.getEndTime() != null && now.compareTo(goodsPromotion.getStartTime()) == 1 && now.compareTo(goodsPromotion.getEndTime()) == -1){
            //有促销,且现在在促销时间内,取促销价格
            price = goodsPromotion.getPromotionPrice();
        }else {
            //无促销，取原价
            price = qstMallGoods.getOriginalPrice();
        }
        return price;
    }

    @Override
    public QstMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        QstMallOrder qstMallOrder = qstMallOrderMapper.selectByOrderNo(orderNo);
        if (qstMallOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (userId.longValue()!= qstMallOrder.getUserId().longValue()){
                return null;
            }
            List<QstMallOrderItem> orderItems = qstMallOrderItemMapper.selectByOrderId(qstMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<QstMallOrderItemVO> qstMallOrderItemVOS = BeanUtil.copyList(orderItems, QstMallOrderItemVO.class);
                QstMallOrderDetailVO qstMallOrderDetailVO = new QstMallOrderDetailVO();
                BeanUtil.copyProperties(qstMallOrder, qstMallOrderDetailVO);
                qstMallOrderDetailVO.setOrderStatusString(QstMallOrderStatusEnum.getQstMallOrderStatusEnumByStatus(qstMallOrderDetailVO.getOrderStatus()).getName());
                qstMallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(qstMallOrderDetailVO.getPayType()).getName());
                qstMallOrderDetailVO.setQstMallOrderItemVOS(qstMallOrderItemVOS);
                return qstMallOrderDetailVO;
            }
        }
        return null;
    }

    @Override
    public QstMallOrder getQstMallOrderByOrderNo(String orderNo) {
        return qstMallOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = qstMallOrderMapper.getTotalQstMallOrders(pageUtil);
        List<QstMallOrder> qstMallOrders = qstMallOrderMapper.findQstMallOrderList(pageUtil);
        List<QstMallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //数据转换 将实体类转成vo
            orderListVOS = BeanUtil.copyList(qstMallOrders, QstMallOrderListVO.class);
            //设置订单状态中文显示值
            for (QstMallOrderListVO qstMallOrderListVO : orderListVOS) {
                qstMallOrderListVO.setOrderStatusString(QstMallOrderStatusEnum.getQstMallOrderStatusEnumByStatus(qstMallOrderListVO.getOrderStatus()).getName());
                qstMallOrderListVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(qstMallOrderListVO.getPayType()).getName());
                AfterSaleStatus afterSaleStatus = qstMallOrderListVO.getAfterSaleStatus();
                String afterSaleStatusStr = afterSaleStatus != null ? afterSaleStatus.toString() : "";
                String afterSaleStatusString = afterSaleStatus != null ? afterSaleStatus.getMessage() : "";
                qstMallOrderListVO.setAfterSaleStatusStr(afterSaleStatusStr);
                qstMallOrderListVO.setAfterSaleStatusString(afterSaleStatusString);
            }
            List<Long> orderIds = qstMallOrders.stream().map(QstMallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<QstMallOrderItem> orderItems = qstMallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<QstMallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(QstMallOrderItem::getOrderId));
                for (QstMallOrderListVO qstMallOrderListVO : orderListVOS) {
                    //封装每个订单列表对象的订单项数据
                    if (itemByOrderIdMap.containsKey(qstMallOrderListVO.getOrderId())) {
                        List<QstMallOrderItem> orderItemListTemp = itemByOrderIdMap.get(qstMallOrderListVO.getOrderId());
                        //将QstMallOrderItem对象列表转换成QstMallOrderItemVO对象列表
                        List<QstMallOrderItemVO> qstMallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, QstMallOrderItemVO.class);
                        qstMallOrderListVO.setQstMallOrderItemVOS(qstMallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        QstMallOrder qstMallOrder = qstMallOrderMapper.selectByOrderNo(orderNo);
        if (qstMallOrder != null) {
            //验证是否是当前userId下的订单，否则报错
            if (userId.longValue()!= qstMallOrder.getUserId().longValue()){
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            //订单状态判断
            if (qstMallOrder.getOrderStatus()<(byte) QstMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()
                    || qstMallOrder.getOrderStatus()>(byte) QstMallOrderStatusEnum.OREDER_EXPRESS.getOrderStatus()){
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            if (qstMallOrderMapper.closeOrder(Collections.singletonList(qstMallOrder.getOrderId()), QstMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        QstMallOrder qstMallOrder = qstMallOrderMapper.selectByOrderNo(orderNo);
        if (qstMallOrder != null) {
            //todo 验证是否是当前userId下的订单，否则报错
            //todo 订单状态判断
            qstMallOrder.setOrderStatus((byte) QstMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            qstMallOrder.setUpdateTime(new Date());
            if (qstMallOrderMapper.updateByPrimaryKeySelective(qstMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        QstMallOrder qstMallOrder = qstMallOrderMapper.selectByOrderNo(orderNo);
        if (qstMallOrder != null) {
            //订单状态判断 非待支付状态下不进行修改操作
            if((byte) QstMallOrderStatusEnum.ORDER_PRE_PAY.getOrderStatus()!= qstMallOrder.getOrderStatus()){
                return ServiceResultEnum.ORDER_STATUS_ERROR.getResult();
            }
            qstMallOrder.setOrderStatus((byte) QstMallOrderStatusEnum.OREDER_PAID.getOrderStatus());
            qstMallOrder.setPayType((byte) payType);
            qstMallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            qstMallOrder.setPayTime(new Date());
            qstMallOrder.setUpdateTime(new Date());
            if (qstMallOrderMapper.updateByPrimaryKeySelective(qstMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public List<QstMallOrderItemVO> getOrderItems(Long id) {
        QstMallOrder qstMallOrder = qstMallOrderMapper.selectByPrimaryKey(id);
        if (qstMallOrder != null) {
            List<QstMallOrderItem> orderItems = qstMallOrderItemMapper.selectByOrderId(qstMallOrder.getOrderId());
            //获取订单项数据
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<QstMallOrderItemVO> qstMallOrderItemVOS = BeanUtil.copyList(orderItems, QstMallOrderItemVO.class);
                return qstMallOrderItemVOS;
            }
        }
        return null;
    }

    @Override
    public String afterSaleApply(Long orderId) {
        Map<String,Object> param = new HashMap<>();
        param.put("orderId", orderId);
        param.put("afterSaleStatus", AfterSaleStatus.APPLY.toString());
        qstMallOrderMapper.updateAfterSaleStatusById(param);
        return ServiceResultEnum.SUCCESS.getResult();
    }

    @Override
    public String acceptAfterSale(Long orderId, Integer accept) {
        Map<String,Object> param = new HashMap<>();
        param.put("orderId", orderId);
        AfterSaleStatus saleStatus = accept == 1 ? AfterSaleStatus.WAIT_RECEIVE : AfterSaleStatus.REFUSED;
        param.put("afterSaleStatus", saleStatus.toString());
        qstMallOrderMapper.updateAfterSaleStatusById(param);
        return ServiceResultEnum.SUCCESS.getResult();
    }

    @Override
    public String afterSaleTransNoSave(Long orderId, Long afterSaleTransNo) {
        Map<String,Object> param = new HashMap<>();
        param.put("orderId", orderId);
        param.put("afterSaleTransNo", afterSaleTransNo);
        qstMallOrderMapper.updateAfterSaleTransNoById(param);
        return ServiceResultEnum.SUCCESS.getResult();
    }

    @Override
    public String confirmAfterSaleReceive(Long orderId) {
        Map<String,Object> param = new HashMap<>();
        param.put("orderId", orderId);
        param.put("afterSaleStatus", AfterSaleStatus.FINISH.toString());
        qstMallOrderMapper.updateAfterSaleStatusById(param);

        List<Long> ids = new ArrayList<>();
        ids.add(orderId);
        qstMallOrderMapper.closeOrder(ids, QstMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus());
        return ServiceResultEnum.SUCCESS.getResult();
    }

    @Override
    public Result<CategoryStatisticsVO> categoryStatistics(Map<String, Object> params) {
        List<CategoryStatisticsSqlVO> statisticResultList = qstMallOrderItemMapper.categoryStatistics(params);
        List<String> categoryNames = new ArrayList<>();
        List<Integer> prices = new ArrayList<>();
        for (CategoryStatisticsSqlVO sqlVO : statisticResultList){
            categoryNames.add(sqlVO.getCategoryName());
            Integer price = sqlVO.getPrice();
            prices.add(price != null ? price : new Integer(0));
        }
        CategoryStatisticsVO statisticsVO = new CategoryStatisticsVO();
        statisticsVO.setCategoryNames(categoryNames);
        statisticsVO.setPrices(prices);
        return ResultGenerator.genSuccessResult(statisticsVO);
    }
}
