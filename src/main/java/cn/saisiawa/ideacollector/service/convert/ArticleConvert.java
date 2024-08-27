package cn.saisiawa.ideacollector.service.convert;

import cn.saisiawa.ideacollector.domain.entity.Article;
import cn.saisiawa.ideacollector.domain.entity.ArticleComment;
import cn.saisiawa.ideacollector.domain.entity.ArticleInfo;
import cn.saisiawa.ideacollector.domain.model.ArticleCommentExt;
import cn.saisiawa.ideacollector.domain.req.ArticleCreateCommentReq;
import cn.saisiawa.ideacollector.domain.req.ArticleCreateReq;
import cn.saisiawa.ideacollector.domain.vo.ArticleCommentVo;
import cn.saisiawa.ideacollector.domain.vo.ArticleVersionHistoryVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.List;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 9:55
 * @Version：1.0
 */
@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ArticleConvert {

    @Mapping(target = "articleId", source = "id")
    ArticleInfo toArticleInfo(ArticleCreateReq req);

    Article toArticle(ArticleCreateReq createReq);


    @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ArticleVersionHistoryVo toArticleVersionHistoryVo(ArticleInfo info);

    List<ArticleVersionHistoryVo> toArticleVersionHistoryVo(List<ArticleInfo> info);

    ArticleComment toArticleComment(ArticleCreateCommentReq req);

    @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    ArticleCommentVo toArticleCommentVo(ArticleCommentExt comment);

    List<ArticleCommentVo> toArticleCommentVo(List<ArticleCommentExt> comment);
}
