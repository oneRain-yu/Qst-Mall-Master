package wxd.qst.mall.service;

import wxd.qst.mall.entity.QstMallGoods;
import wxd.qst.mall.util.PageQueryUtil;
import wxd.qst.mall.util.PageResult;

import java.util.List;

public interface QstMallGoodsService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getQstMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     *
     * @param goods
     * @return
     */
    String saveQstMallGoods(QstMallGoods goods);

    /**
     * 批量新增商品数据
     *
     * @param qstMallGoodsList
     * @return
     */
    void batchSaveQstMallGoods(List<QstMallGoods> qstMallGoodsList);

    /**
     * 修改商品信息
     *
     * @param goods
     * @return
     */
    String updateQstMallGoods(QstMallGoods goods);

    /**
     * 获取商品详情
     *
     * @param id
     * @return
     */
    QstMallGoods getQstMallGoodsById(Long id);

    /**
     * 批量修改销售状态(上架下架)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids,int sellStatus);

    /**
     * 商品搜索
     *
     * @param pageUtil
     * @return
     */
    PageResult searchQstMallGoods(PageQueryUtil pageUtil);

    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getQstMallCommentPage(PageQueryUtil pageUtil);
}
