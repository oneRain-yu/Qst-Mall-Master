package wxd.qst.mall.service;

import wxd.qst.mall.controller.vo.QstMallGoodsCommentVO;

import java.util.List;

public interface QstMallGoodsCommentService {
    /**
     * 保存会员评价
     * @param userId
     * @param goodsId
     * @param content
     * @return
     */
    String saveMallUserComment(Long userId, Long goodsId, String content);

    /**
     * 保存管理员回复
     * @param userId
     * @param commentId
     * @param content
     * @return
     */
    String saveAdminUserComment(Long userId, Long commentId, String content);

    /**
     * 删除评价及其回复
     * @param ids
     * @return
     */
    String deleteComment(Long[] ids);

    /**
     * 通过goodsId查找评论
     * @param goodsId
     * @return
     */
    List<QstMallGoodsCommentVO> findCommentByGoodsId(Long goodsId);
}
