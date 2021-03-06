package wxd.qst.mall.controller.admin;

import wxd.qst.mall.common.Constants;
import wxd.qst.mall.common.QstMallCategoryLevelEnum;
import wxd.qst.mall.common.ServiceResultEnum;
import wxd.qst.mall.controller.vo.QstMallUserVO;
import wxd.qst.mall.entity.CommentType;
import wxd.qst.mall.entity.GoodsCategory;
import wxd.qst.mall.entity.QstMallGoods;
import wxd.qst.mall.service.QstMallCategoryService;
import wxd.qst.mall.service.QstMallGoodsCommentService;
import wxd.qst.mall.service.QstMallGoodsService;
import wxd.qst.mall.util.PageQueryUtil;
import wxd.qst.mall.util.Result;
import wxd.qst.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Controller
@RequestMapping("/admin")
public class QstMallGoodsController {

    @Resource
    private QstMallGoodsService qstMallGoodsService;
    @Resource
    private QstMallCategoryService qstMallCategoryService;
    @Resource
    private QstMallGoodsCommentService qstMallGoodsCommentService;

    @GetMapping("/goods")
    public String goodsPage(HttpServletRequest request) {
        request.setAttribute("path", "qst_mall_goods");
        return "admin/qst_mall_goods";
    }

    @GetMapping("/goods/comments")
    public String goodsCommentsPage(HttpServletRequest request) {
        request.setAttribute("path", "qst_mall_comments");
        return "admin/qst_mall_comments";
    }

    @GetMapping("/goods/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        //查询所有的一级分类
        List<GoodsCategory> firstLevelCategories = qstMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), QstMallCategoryLevelEnum.LEVEL_ONE.getLevel());
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            //查询一级分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories = qstMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), QstMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = qstMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), QstMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                request.setAttribute("firstLevelCategories", firstLevelCategories);
                request.setAttribute("secondLevelCategories", secondLevelCategories);
                request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                request.setAttribute("path", "goods-edit");
                return "admin/qst_mall_goods_edit";
            }
        }
        return "error/error_5xx";
    }

    @GetMapping("/goods/edit/{goodsId}")
    public String edit(HttpServletRequest request, @PathVariable("goodsId") Long goodsId) {
        request.setAttribute("path", "edit");
        QstMallGoods qstMallGoods = qstMallGoodsService.getQstMallGoodsById(goodsId);
        if (qstMallGoods == null) {
            return "error/error_400";
        }
        if (qstMallGoods.getGoodsCategoryId() > 0) {
            if (qstMallGoods.getGoodsCategoryId() != null || qstMallGoods.getGoodsCategoryId() > 0) {
                //有分类字段则查询相关分类数据返回给前端以供分类的三级联动显示
                GoodsCategory currentGoodsCategory = qstMallCategoryService.getGoodsCategoryById(qstMallGoods.getGoodsCategoryId());
                //商品表中存储的分类id字段为三级分类的id，不为三级分类则是错误数据
                if (currentGoodsCategory != null && currentGoodsCategory.getCategoryLevel() == QstMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
                    //查询所有的一级分类
                    List<GoodsCategory> firstLevelCategories = qstMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), QstMallCategoryLevelEnum.LEVEL_ONE.getLevel());
                    //根据parentId查询当前parentId下所有的三级分类
                    List<GoodsCategory> thirdLevelCategories = qstMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(currentGoodsCategory.getParentId()), QstMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    //查询当前三级分类的父级二级分类
                    GoodsCategory secondCategory = qstMallCategoryService.getGoodsCategoryById(currentGoodsCategory.getParentId());
                    if (secondCategory != null) {
                        //根据parentId查询当前parentId下所有的二级分类
                        List<GoodsCategory> secondLevelCategories = qstMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondCategory.getParentId()), QstMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                        //查询当前二级分类的父级一级分类
                        GoodsCategory firestCategory = qstMallCategoryService.getGoodsCategoryById(secondCategory.getParentId());
                        if (firestCategory != null) {
                            //所有分类数据都得到之后放到request对象中供前端读取
                            request.setAttribute("firstLevelCategories", firstLevelCategories);
                            request.setAttribute("secondLevelCategories", secondLevelCategories);
                            request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                            request.setAttribute("firstLevelCategoryId", firestCategory.getCategoryId());
                            request.setAttribute("secondLevelCategoryId", secondCategory.getCategoryId());
                            request.setAttribute("thirdLevelCategoryId", currentGoodsCategory.getCategoryId());
                        }
                    }
                }
            }
        }
        if (qstMallGoods.getGoodsCategoryId() == 0) {
            //查询所有的一级分类
            List<GoodsCategory> firstLevelCategories = qstMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), QstMallCategoryLevelEnum.LEVEL_ONE.getLevel());
            if (!CollectionUtils.isEmpty(firstLevelCategories)) {
                //查询一级分类列表中第一个实体的所有二级分类
                List<GoodsCategory> secondLevelCategories = qstMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), QstMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                    //查询二级分类列表中第一个实体的所有三级分类
                    List<GoodsCategory> thirdLevelCategories = qstMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), QstMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    request.setAttribute("firstLevelCategories", firstLevelCategories);
                    request.setAttribute("secondLevelCategories", secondLevelCategories);
                    request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                }
            }
        }
        request.setAttribute("goods", qstMallGoods);
        request.setAttribute("path", "goods-edit");
        return "admin/qst_mall_goods_edit";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(qstMallGoodsService.getQstMallGoodsPage(pageUtil));
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/goods/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody QstMallGoods qstMallGoods) {
        if (StringUtils.isEmpty(qstMallGoods.getGoodsName())
                || StringUtils.isEmpty(qstMallGoods.getGoodsIntro())
                || StringUtils.isEmpty(qstMallGoods.getTag())
                || Objects.isNull(qstMallGoods.getGoodsCategoryId())
                || Objects.isNull(qstMallGoods.getStockNum())
                || Objects.isNull(qstMallGoods.getGoodsSellStatus())
                || StringUtils.isEmpty(qstMallGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(qstMallGoods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = qstMallGoodsService.saveQstMallGoods(qstMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * 修改
     */
    @RequestMapping(value = "/goods/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody QstMallGoods qstMallGoods) {
        if (Objects.isNull(qstMallGoods.getGoodsId())
                || StringUtils.isEmpty(qstMallGoods.getGoodsName())
                || StringUtils.isEmpty(qstMallGoods.getGoodsIntro())
                || StringUtils.isEmpty(qstMallGoods.getTag())
                || Objects.isNull(qstMallGoods.getGoodsCategoryId())
                || Objects.isNull(qstMallGoods.getStockNum())
                || Objects.isNull(qstMallGoods.getGoodsSellStatus())
                || StringUtils.isEmpty(qstMallGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(qstMallGoods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = qstMallGoodsService.updateQstMallGoods(qstMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * 详情
     */
    @GetMapping("/goods/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        QstMallGoods goods = qstMallGoodsService.getQstMallGoodsById(id);
        if (goods == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(goods);
    }

    /**
     * 批量修改销售状态
     */
    @RequestMapping(value = "/goods/status/{sellStatus}", method = RequestMethod.PUT)
    @ResponseBody
    public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") int sellStatus) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
            return ResultGenerator.genFailResult("状态异常！");
        }
        if (qstMallGoodsService.batchUpdateSellStatus(ids, sellStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }


    /**
     * 评价列表
     */
    @RequestMapping(value = "/goods/comment/list", method = RequestMethod.GET)
    @ResponseBody
    public Result commentList(@RequestParam Map<String, Object> params) {
        params.put("paramCommentType", CommentType.MALL_USER_COMMENT.toString());
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(qstMallGoodsService.getQstMallCommentPage(pageUtil));
    }

    /**
     * 管理员回复评价
     * @param commentId
     * @param content
     * @param httpSession
     * @return
     */
    @PostMapping("/goods/recomment/save")
    @ResponseBody
    public Result saveGoodsReComment(@RequestParam Long commentId, @RequestParam String content, HttpSession httpSession) {
        Long userId = ((Integer)httpSession.getAttribute("loginUserId")).longValue();
        String saveResult = qstMallGoodsCommentService.saveAdminUserComment(userId, commentId, content);
        //添加成功
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //添加失败
        return ResultGenerator.genFailResult(saveResult);
    }

    /**
     * 删除评价
     * @param ids
     * @return
     */
    @RequestMapping(value = "/goods/comment/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result deleteComment(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = qstMallGoodsCommentService.deleteComment(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

}