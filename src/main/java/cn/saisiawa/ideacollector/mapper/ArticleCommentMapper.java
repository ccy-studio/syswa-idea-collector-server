package cn.saisiawa.ideacollector.mapper;

import cn.saisiawa.ideacollector.domain.entity.ArticleComment;
import cn.saisiawa.ideacollector.domain.model.ArticleCommentExt;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户评论表 Mapper 接口
 * </p>
 *
 * @author Saisaiwa
 * @since 2024-07-11
 */
public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {

    /**
     * 按文章id查询评论
     *
     * @param id       文章ID
     * @param parentId 评论的父ID
     * @return {@link IPage}<{@link ArticleCommentExt}>
     */
    IPage<ArticleCommentExt> selectCommentByArticleId(Page page, @Param("id") Long id, @Param("parentId") Long parentId);

    /**
     * 评论点赞
     *
     * @param id
     * @return
     */
    int increaseGive(Long id);
}
