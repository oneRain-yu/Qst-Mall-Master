package wxd.qst.mall.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.stereotype.Service;
import wxd.qst.mall.controller.vo.PromotionItemVO;
import wxd.qst.mall.entity.Promotion;
import wxd.qst.mall.util.PageQueryUtil;
import wxd.qst.mall.util.PageResult;

import java.util.List;

public interface QstMallPromotionService {

    PageResult getQstMallPromotionPage(PageQueryUtil pageUtil);

    List<PromotionItemVO> getActivatedPromotions(int number);

    Promotion getPromotionsByGoodsId(Long goodsId);

    Promotion getActivatedPromotion(Long goodsId);

    String saveQstMallPromotion(Promotion promotion);

    Boolean batchDelete(Long[] ids);
}
