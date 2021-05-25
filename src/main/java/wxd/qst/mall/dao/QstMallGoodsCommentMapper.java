package wxd.qst.mall.dao;

import org.apache.ibatis.annotations.Param;
import wxd.qst.mall.controller.vo.QstMallGoodsCommentVO;
import wxd.qst.mall.entity.GoodsComment;
import wxd.qst.mall.entity.GoodsCommentDTO;
import wxd.qst.mall.entity.QstMallGoods;
import wxd.qst.mall.util.PageQueryUtil;

import java.util.List;
import java.util.Map;

public interface QstMallGoodsCommentMapper {
    void insert(GoodsComment comment);

    List<GoodsCommentDTO> findQstMallCommentList(PageQueryUtil pageUtil);

    int getTotalQstMallComment(PageQueryUtil pageUtil);

    void deleteCommentByIds(@Param("commentIds") List<Long> ids);

    List<QstMallGoodsCommentVO> queryMallUserCommentByGoodsId(Map<String, Object> param);

    List<QstMallGoodsCommentVO> queryAdminUserCommentByParentId(Map<String, Object> param);
}
