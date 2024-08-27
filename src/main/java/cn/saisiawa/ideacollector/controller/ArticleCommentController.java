package cn.saisiawa.ideacollector.controller;

import cn.saisiawa.ideacollector.common.bean.BaseResponse;
import cn.saisiawa.ideacollector.common.bean.PageBodyResponse;
import cn.saisiawa.ideacollector.domain.req.ArticleCommentReq;
import cn.saisiawa.ideacollector.domain.req.ArticleCreateCommentReq;
import cn.saisiawa.ideacollector.domain.vo.ArticleCommentVo;
import cn.saisiawa.ideacollector.service.ArticleCommentService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文章评论
 *
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 15:22
 * @Version：1.0
 */
@RestController
@RequestMapping("/article/comment")
@Validated
public class ArticleCommentController {

    @Resource
    private ArticleCommentService commentService;


    /**
     * 添加评论
     *
     * @param req
     */
    @PostMapping("/add")
    public BaseResponse<Void> addComment(@RequestBody @Validated ArticleCreateCommentReq req) {
        commentService.addComment(req);
        return BaseResponse.ok();
    }

    /**
     * 评论点赞
     *
     * @param id
     */
    @RequestMapping("/give/{id}")
    public BaseResponse<Void> increaseGive(@NotNull @PathVariable Long id) {
        commentService.increaseGive(id);
        return BaseResponse.ok();
    }

    /**
     * 获取文章的评论
     *
     * @param req
     * @return
     */
    @RequestMapping("/list")
    public BaseResponse<PageBodyResponse<ArticleCommentVo>> getArticleCommentById(ArticleCommentReq req) {
        return BaseResponse.ok(commentService.getArticleCommentById(req));
    }
}
