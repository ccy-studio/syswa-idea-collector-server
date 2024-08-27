package cn.saisiawa.ideacollector.controller;

import cn.saisiawa.ideacollector.common.bean.BaseResponse;
import cn.saisiawa.ideacollector.common.bean.PageBodyResponse;
import cn.saisiawa.ideacollector.common.group.InsertGroup;
import cn.saisiawa.ideacollector.common.group.UpdateGroup;
import cn.saisiawa.ideacollector.common.util.ValidationUtils;
import cn.saisiawa.ideacollector.domain.req.ArticleCreateReq;
import cn.saisiawa.ideacollector.domain.req.ArticleQueryReq;
import cn.saisiawa.ideacollector.domain.vo.ArticleInfoVo;
import cn.saisiawa.ideacollector.domain.vo.ArticleListVo;
import cn.saisiawa.ideacollector.domain.vo.ArticleVersionHistoryVo;
import cn.saisiawa.ideacollector.service.ArticleService;
import jakarta.annotation.Resource;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章
 *
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 14:03
 * @Version：1.0
 */
@RestController
@RequestMapping("/article")
@Validated
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private Validator validator;


    /**
     * 发布创意文章
     *
     * @param req
     */
    @PostMapping("/publish")
    public BaseResponse<Long> publishArticle(@RequestBody ArticleCreateReq req) {
        if (req.getId() == null) {
            ValidationUtils.validate(req, validator, InsertGroup.class);
        } else {
            ValidationUtils.validate(req, validator, UpdateGroup.class);
        }
        return BaseResponse.ok(articleService.publishArticle(req));
    }

    /**
     * 结束创意文章的话题讨论
     *
     * @param id
     */
    @RequestMapping("/close")
    public BaseResponse<Void> closeActivityArticle(@NotNull Long id) {
        articleService.closeActivityArticle(id);
        return BaseResponse.ok();
    }

    /**
     * 增加阅读数
     *
     * @param id
     */
    @RequestMapping("/view")
    public BaseResponse<Void> increaseViewCount(@NotNull Long id) {
        articleService.increaseViewCount(id);
        return BaseResponse.ok();
    }

    /**
     * 增加点赞数
     *
     * @param id
     */
    @RequestMapping("/give")
    public BaseResponse<Void> increaseGiveCount(@NotNull Long id) {
        articleService.increaseGiveCount(id);
        return BaseResponse.ok();
    }

    /**
     * 返回列表数据
     *
     * @param req
     * @return
     */
    @RequestMapping("/page-list")
    public BaseResponse<PageBodyResponse<ArticleListVo>> pageList(ArticleQueryReq req) {
        return BaseResponse.ok(articleService.pageList(req));
    }

    /**
     * 根据ID查询详细信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/get/{id}")
    public BaseResponse<ArticleInfoVo> getDetailById(@PathVariable("id") Long id) {
        return BaseResponse.ok(articleService.getDetailById(id));
    }


    /**
     * 返回历史版本信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/history/{id}")
    public BaseResponse<List<ArticleVersionHistoryVo>> selectVersionHistoryAll(@PathVariable("id") Long id) {
        return BaseResponse.ok(articleService.selectVersionHistoryAll(id));
    }

}
