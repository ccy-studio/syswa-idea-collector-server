package cn.saisiawa.ideacollector.service;

import cn.hutool.core.lang.Assert;
import cn.saisiawa.ideacollector.common.bean.PageBodyResponse;
import cn.saisiawa.ideacollector.common.exception.BizException;
import cn.saisiawa.ideacollector.domain.entity.Article;
import cn.saisiawa.ideacollector.domain.entity.ArticleInfo;
import cn.saisiawa.ideacollector.domain.req.ArticleCreateReq;
import cn.saisiawa.ideacollector.domain.req.ArticleQueryReq;
import cn.saisiawa.ideacollector.domain.vo.ArticleInfoVo;
import cn.saisiawa.ideacollector.domain.vo.ArticleListVo;
import cn.saisiawa.ideacollector.domain.vo.ArticleVersionHistoryVo;
import cn.saisiawa.ideacollector.mapper.ArticleInfoMapper;
import cn.saisiawa.ideacollector.mapper.ArticleMapper;
import cn.saisiawa.ideacollector.service.convert.ArticleConvert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 9:48
 * @Version：1.0
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleInfoMapper articleInfoMapper;

    @Resource
    private ArticleConvert convert;


    /**
     * 发布创意文章
     *
     * @param req
     */
    public Long publishArticle(ArticleCreateReq req) {
        ArticleInfo articleInfo = convert.toArticleInfo(req);
        articleInfo.setId(null);
        if (req.getId() != null) {
            //update
            Article article = articleMapper.selectById(req.getId());
            Assert.notNull(article);
            if (article.getStatus().equals(1)) {
                throw new BizException("此话题已经结束讨论");
            }
        } else {
            //insert
            Article article = convert.toArticle(req);
            article.setUserId(SessionService.getSession().getId());
            article.setStatus(0);
            article.setCreateTime(LocalDateTime.now());
            articleMapper.insert(article);
            articleInfo.setArticleId(article.getId());
        }
        articleInfo.setCreateTime(LocalDateTime.now());
        articleInfoMapper.insert(articleInfo);
        return articleInfo.getArticleId();
    }


    /**
     * 结束创意文章的话题讨论
     *
     * @param id
     */
    public void closeActivityArticle(Long id) {
        Article article = articleMapper.selectById(id);
        Assert.notNull(article);
        if (!article.getUserId().equals(SessionService.getSession().getId())) {
            throw new BizException("文章不存在");
        }
        article.setStatus(1);
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.updateById(article);
    }

    /**
     * 增加阅读数
     *
     * @param id
     */
    public void increaseViewCount(Long id) {
        articleMapper.increaseSee(id);
    }

    /**
     * 增加点赞数
     *
     * @param id
     */
    public void increaseGiveCount(Long id) {
        articleMapper.increaseGive(id);
    }


    /**
     * 返回列表数据
     *
     * @param req
     * @return
     */
    public PageBodyResponse<ArticleListVo> pageList(ArticleQueryReq req) {
        IPage<ArticleListVo> page = articleMapper.selectSmallList(req, req.getQuery(), req.getStatus(), req.getSortType(),
                Objects.equals(1, req.getSelf()) ? SessionService.getSession().getId() : null);
        return PageBodyResponse.convert(page);
    }


    /**
     * 根据ID查询详细信息
     *
     * @param id
     * @return
     */
    public ArticleInfoVo getDetailById(Long id) {
        return articleMapper.getArticleInfo(id);
    }


    /**
     * 返回历史版本信息
     *
     * @param id
     * @return
     */
    public List<ArticleVersionHistoryVo> selectVersionHistoryAll(Long id) {
        List<ArticleInfo> articleInfos = articleInfoMapper.selectList(Wrappers.lambdaQuery(ArticleInfo.class)
                .eq(ArticleInfo::getArticleId, id)
                .orderByAsc(ArticleInfo::getCreateTime)
        );
        return convert.toArticleVersionHistoryVo(articleInfos);
    }

}
