package cn.saisiawa.ideacollector.mapper;

import cn.saisiawa.ideacollector.domain.entity.Article;
import cn.saisiawa.ideacollector.domain.vo.ArticleInfoVo;
import cn.saisiawa.ideacollector.domain.vo.ArticleListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 创意主表 Mapper 接口
 * </p>
 *
 * @author Saisaiwa
 * @since 2024-07-11
 */
public interface ArticleMapper extends BaseMapper<Article> {

    int increaseSee(Long id);

    int increaseGive(Long id);


    /**
     * 查询文章列表数据
     *
     * @param page     分页
     * @param keyword  关键字查询
     * @param sortType 排序条件：0默认综合排序，1点赞排序，2时间排序
     * @return
     */
    IPage<ArticleListVo> selectSmallList(Page page, @Param("keyword") String keyword, @Param("status") Integer status, @Param("sortType") Integer sortType, @Param("userId") Long userId);

    /**
     * 查询详细文章的信息
     *
     * @param id
     * @return
     */
    ArticleInfoVo getArticleInfo(Long id);
}
