package wxd.qst.mall.controller.admin;

import wxd.qst.mall.common.ServiceResultEnum;
import wxd.qst.mall.controller.vo.CategoryStatisticsVO;
import wxd.qst.mall.controller.vo.QstMallOrderItemVO;
import wxd.qst.mall.entity.QstMallOrder;
import wxd.qst.mall.service.QstMallOrderService;
import wxd.qst.mall.util.PageQueryUtil;
import wxd.qst.mall.util.Result;
import wxd.qst.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
@RequestMapping("/admin")
public class QstMallOrderController {

    @Resource
    private QstMallOrderService qstMallOrderService;

    @GetMapping("/orders")
    public String ordersPage(HttpServletRequest request) {
        request.setAttribute("path", "orders");
        return "admin/qst_mall_order";
    }

    @GetMapping("/order/statistics")
    public String orderStatisticsPage(HttpServletRequest request) {
        request.setAttribute("path", "statistics");
        return "admin/qst_mall_statistics";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/orders/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(qstMallOrderService.getQstMallOrdersPage(pageUtil));
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/orders/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody QstMallOrder qstMallOrder) {
        if (Objects.isNull(qstMallOrder.getTotalPrice())
                || Objects.isNull(qstMallOrder.getOrderId())
                || qstMallOrder.getOrderId() < 1
                || qstMallOrder.getTotalPrice() < 1
                || StringUtils.isEmpty(qstMallOrder.getUserAddress())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = qstMallOrderService.updateOrderInfo(qstMallOrder);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/order-items/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        List<QstMallOrderItemVO> orderItems = qstMallOrderService.getOrderItems(id);
        if (!CollectionUtils.isEmpty(orderItems)) {
            return ResultGenerator.genSuccessResult(orderItems);
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
    }

    /**
     * 配货
     */
    @RequestMapping(value = "/orders/checkDone", method = RequestMethod.POST)
    @ResponseBody
    public Result checkDone(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = qstMallOrderService.checkDone(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 出库
     */
    @RequestMapping(value = "/orders/checkOut", method = RequestMethod.POST)
    @ResponseBody
    public Result checkOut(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = qstMallOrderService.checkOut(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/orders/close", method = RequestMethod.POST)
    @ResponseBody
    public Result closeOrder(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = qstMallOrderService.closeOrder(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }
    /**
     * 申请审核
     */
    @RequestMapping(value = "/orders/aftersale/accept", method = RequestMethod.POST)
    @ResponseBody
    public Result acceptAfterSale(@RequestParam Long orderId, @RequestParam Integer accept) {
        String result = qstMallOrderService.acceptAfterSale(orderId, accept);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }
    /**
     * 售后确认收货
     */
    @RequestMapping(value = "/orders/aftersale/receive/confirm", method = RequestMethod.POST)
    @ResponseBody
    public Result confirmAfterSaleReceive(@RequestParam Long orderId) {
        String result = qstMallOrderService.confirmAfterSaleReceive(orderId);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 统计
     */
    @RequestMapping(value = "/order/category/statistics", method = RequestMethod.GET)
    @ResponseBody
    public Result categoryStatistics(@RequestParam Map<String, Object> params) {
        Result<CategoryStatisticsVO> result = qstMallOrderService.categoryStatistics(params);
        return result;
    }


}