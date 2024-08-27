package cn.saisiawa.ideacollector.service;

import cn.hutool.core.lang.Assert;
import cn.saisiawa.ideacollector.common.bean.PageBodyResponse;
import cn.saisiawa.ideacollector.domain.entity.ArticleComment;
import cn.saisiawa.ideacollector.domain.model.ArticleCommentExt;
import cn.saisiawa.ideacollector.domain.req.ArticleCommentReq;
import cn.saisiawa.ideacollector.domain.req.ArticleCreateCommentReq;
import cn.saisiawa.ideacollector.domain.vo.ArticleCommentVo;
import cn.saisiawa.ideacollector.mapper.ArticleCommentMapper;
import cn.saisiawa.ideacollector.mapper.ArticleMapper;
import cn.saisiawa.ideacollector.service.convert.ArticleConvert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 14:59
 * @Version：1.0
 */
@Service
@Slf4j
public class ArticleCommentService {

    @Resource
    private ArticleCommentMapper articleCommentMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleConvert convert;


    /**
     * 添加评论
     *
     * @param req
     */
    public void addComment(ArticleCreateCommentReq req) {
        Assert.notNull(articleMapper.selectById(req.getId()));
        ArticleComment articleComment = convert.toArticleComment(req);
        articleComment.setId(null);
        articleComment.setUserId(SessionService.getSession().getId());
        articleComment.setArticleId(req.getId());
        if(req.getParentId()!=null){
            Assert.notNull(articleCommentMapper.selectById(req.getParentId()));
            articleComment.setParentId(req.getParentId());
        }
        articleComment.setCreateTime(LocalDateTime.now());
        articleComment.setGive(0L);
        articleCommentMapper.insert(articleComment);
    }


    /**
     * 评论点赞
     *
     * @param id
     */
    public void increaseGive(Long id) {
        articleCommentMapper.increaseGive(id);
    }


    /**
     * 获取文章的评论
     *
     * @param req
     * @return
     */
    public PageBodyResponse<ArticleCommentVo> getArticleCommentById(ArticleCommentReq req) {
        IPage<ArticleCommentExt> rootComment = articleCommentMapper.selectCommentByArticleId(req, req.getArticleId(), null);
        List<ArticleCommentVo> commentVoList = convert.toArticleCommentVo(rootComment.getRecords());
        commentVoList.forEach(v -> {
            List<ArticleCommentVo> secondsList = new ArrayList<>();
            loopSetComment(secondsList, v, req.getArticleId());
            v.setChildComment(secondsList);
        });
        return PageBodyResponse.convert(rootComment, commentVoList);
    }

    private void loopSetComment(List<ArticleCommentVo> arr, ArticleCommentVo root, Long aId) {
        IPage<ArticleCommentExt> list = articleCommentMapper.selectCommentByArticleId(new Page(1, -1), aId, root.getId());
        if (!list.getRecords().isEmpty()) {
            List<ArticleCommentVo> commentVos = convert.toArticleCommentVo(list.getRecords());
            arr.addAll(commentVos);
            commentVos.forEach(v -> {
                v.setParentUserName(root.getUserNickName());
                loopSetComment(arr, v, aId);
            });
        }
    }

}
