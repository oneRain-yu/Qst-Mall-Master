package wxd.qst.mall.controller.mall;

import org.springframework.web.bind.annotation.*;
import wxd.qst.mall.common.Constants;
import wxd.qst.mall.common.ServiceResultEnum;
import wxd.qst.mall.controller.vo.QstMallGoodsCommentVO;
import wxd.qst.mall.controller.vo.QstMallGoodsDetailVO;
import wxd.qst.mall.controller.vo.QstMallUserVO;
import wxd.qst.mall.controller.vo.SearchPageCategoryVO;
import wxd.qst.mall.entity.QstMallGoods;
import wxd.qst.mall.entity.Promotion;
import wxd.qst.mall.entity.QstMallShoppingCartItem;
import wxd.qst.mall.service.QstMallCategoryService;
import wxd.qst.mall.service.QstMallGoodsCommentService;
import wxd.qst.mall.service.QstMallGoodsService;
import wxd.qst.mall.service.QstMallPromotionService;
import wxd.qst.mall.util.BeanUtil;
import wxd.qst.mall.util.PageQueryUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import wxd.qst.mall.util.Result;
import wxd.qst.mall.util.ResultGenerator;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class GoodsController {
    @Resource
    private QstMallGoodsService qstMallGoodsService;
    @Resource
    private QstMallCategoryService qstMallCategoryService;
    @Resource
    private QstMallPromotionService qstMallPromotionService;
    @Resource
    private QstMallGoodsCommentService qstMallGoodsCommentService;

    @GetMapping({"/search", "/search.html"})
    public String searchPage(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        //每页十条
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        //封装分类数据
        if (params.containsKey("goodsCategoryId") && !StringUtils.isEmpty(params.get("goodsCategoryId")+ "")) {
            Long categoryId = Long.valueOf(params.get("goodsCategoryId") + "");
            SearchPageCategoryVO searchPageCategoryVO = qstMallCategoryService.getCategoriesForSearch(categoryId);
            if (searchPageCategoryVO != null) {
                request.setAttribute("goodsCategoryId", categoryId);
                request.setAttribute("searchPageCategoryVO", searchPageCategoryVO);
            }
        }
        //封装参数供前端回显
        if (params.containsKey("orderBy") && !StringUtils.isEmpty(params.get("orderBy") + "")) {
            request.setAttribute("orderBy", params.get("orderBy") + "");
        }
        String keyword = "";
        //对keyword做过滤 去掉空格及其他预定义字符
        if (params.containsKey("keyword") && !StringUtils.isEmpty((params.get("keyword") + "").trim())) {
            keyword = params.get("keyword") + "";
        }
        request.setAttribute("keyword", keyword);
        params.put("keyword", keyword);
        //封装商品数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("pageResult", qstMallGoodsService.searchQstMallGoods(pageUtil));
        return "mall/search";
    }

    @GetMapping("/goods/detail/{goodsId}")
    public String detailPage(@PathVariable("goodsId") Long goodsId, HttpServletRequest request) {
        if (goodsId < 1) {
            return "error/error_5xx";
        }
        QstMallGoods goods = qstMallGoodsService.getQstMallGoodsById(goodsId);
        if (goods == null) {
            return "error/error_404";
        }
        QstMallGoodsDetailVO goodsDetailVO = new QstMallGoodsDetailVO();
        BeanUtil.copyProperties(goods, goodsDetailVO);
        goodsDetailVO.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
        goodsDetailVO.setSellingPrice(goods.getOriginalPrice());
        Promotion promotion=qstMallPromotionService.getPromotionsByGoodsId(goodsId);
        if(promotion!=null){
            goodsDetailVO.setStartTime(promotion.getStartTime());
            goodsDetailVO.setEndTime(promotion.getEndTime());
            if(promotion.getStartTime().after(new Date())){
                goodsDetailVO.setPromotionStatus(Constants.PROMOTION_STATUS_NOT_STARTED);//还未开始
            }else if(promotion.getEndTime().before(new Date())){
                    goodsDetailVO.setPromotionStatus(Constants.PROMOTION_STATUS_END);//已经结束
            }else{
                goodsDetailVO.setPromotionStatus(Constants.PROMOTION_STATUS_STARTED);//正在进行
                goodsDetailVO.setSellingPrice(promotion.getPromotionPrice());
            }
        }else {
            goodsDetailVO.setPromotionStatus(Constants.PROMOTION_STATUS_NOT_STARTED);//已经结束
        }
        request.setAttribute("goodsDetail", goodsDetailVO);
        return "mall/detail";
    }

    /**
     * 会员新增评价
     * @param goodsId
     * @param content
     * @param httpSession
     * @return
     */
    @PostMapping("/goods/comment/save")
    @ResponseBody
    public Result saveGoodsComment(@RequestParam Long goodsId, @RequestParam String content, HttpSession httpSession) {
        QstMallUserVO user = (QstMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);

        Long userId = user.getUserId();
        String saveResult = qstMallGoodsCommentService.saveMallUserComment(userId, goodsId, content);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    /**
     * 通过商品id查询评价
     * @param goodsId
     * @param httpSession
     * @return
     */
    @GetMapping("/goods/comment/findbygoodsid")
    @ResponseBody
    public Result<List<QstMallGoodsCommentVO>> findCommentByGoodsId(@RequestParam Long goodsId, HttpSession httpSession) {
        List<QstMallGoodsCommentVO> commentS = qstMallGoodsCommentService.findCommentByGoodsId(goodsId);
        //查询成功
        Result<List<QstMallGoodsCommentVO>> result = ResultGenerator.genSuccessResult();
        result.setData(commentS);
        return result;
    }






}
