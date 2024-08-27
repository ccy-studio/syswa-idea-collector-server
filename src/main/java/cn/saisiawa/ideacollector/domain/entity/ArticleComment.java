package cn.saisiawa.ideacollector.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户评论表
 * </p>
 *
 * @author Saisaiwa
 * @since 2024-07-11
 */
@Getter
@Setter
@TableName("T_ARTICLE_COMMENT")
public class ArticleComment {

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 创意主表ID
     */
    @TableField("ARTICLE_ID")
    private Long articleId;

    /**
     * 评论用户
     */
    @TableField("USER_ID")
    private Long userId;

    /**
     * 评论内容
     */
    @TableField("CONTENT")
    private String content;

    /**
     * 点赞数
     */
    @TableField("GIVE")
    private Long give;

    /**
     * 父评论ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField("UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 删除标识 0: 未删除
     */
    @TableField("IS_DELETE")
    private Byte isDelete;
}
