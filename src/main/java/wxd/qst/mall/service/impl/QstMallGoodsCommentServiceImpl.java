package wxd.qst.mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wxd.qst.mall.common.ServiceResultEnum;
import wxd.qst.mall.controller.vo.QstMallGoodsCommentVO;
import wxd.qst.mall.dao.QstMallGoodsCommentMapper;
import wxd.qst.mall.entity.CommentType;
import wxd.qst.mall.entity.GoodsComment;
import wxd.qst.mall.service.QstMallGoodsCommentService;
import wxd.qst.mall.util.BeanUtil;

import java.util.*;

@Service
public class QstMallGoodsCommentServiceImpl implements QstMallGoodsCommentService {
    @Autowired
    private QstMallGoodsCommentMapper goodsCommentMapper;

    @Override
    public String saveMallUserComment(Long userId, Long goodsId, String content) {
        GoodsComment comment = new GoodsComment();
        comment.setUserId(userId);
        comment.setGoodsId(goodsId);
        comment.setContent(content);
        comment.setCommentType(CommentType.MALL_USER_COMMENT);
        comment.setDeleted(false);
        comment.setParentCommentId(null);
        comment.setCreateTime(new Date());
        goodsCommentMapper.insert(comment);
        return ServiceResultEnum.SUCCESS.getResult();
    }

    @Override
    public String saveAdminUserComment(Long userId, Long commentId, String content) {
        GoodsComment comment = new GoodsComment();
        comment.setUserId(userId);
        comment.setGoodsId(null);
        comment.setContent(content);
        comment.setCommentType(CommentType.ADMIN_COMMENT);
        comment.setDeleted(false);
        comment.setParentCommentId(commentId);
        comment.setCreateTime(new Date());
        goodsCommentMapper.insert(comment);
        return ServiceResultEnum.SUCCESS.getResult();
    }

    @Override
    public String deleteComment(Long[] ids) {
        goodsCommentMapper.deleteCommentByIds(Arrays.asList(ids));
        return ServiceResultEnum.SUCCESS.getResult();
    }

    @Override
    public List<QstMallGoodsCommentVO> findCommentByGoodsId(Long goodsId) {
        Map<String, Object> param = new HashMap<>();
        param.put("goodsId", goodsId);
        param.put("commentType", CommentType.MALL_USER_COMMENT.toString());
        List<QstMallGoodsCommentVO> mallUserComments = goodsCommentMapper.queryMallUserCommentByGoodsId(param);
        for (QstMallGoodsCommentVO mallUserCommentVo : mallUserComments){
            Long mallUserCommentId = mallUserCommentVo.getId();
            Map<String, Object> param2 = new HashMap<>();
            param2.put("mallUserCommentId", mallUserCommentId);
            List<QstMallGoodsCommentVO> adminUserComments = goodsCommentMapper.queryAdminUserCommentByParentId(param2);
            mallUserCommentVo.setAdminComments(adminUserComments);
        }
        return mallUserComments;
    }
}
