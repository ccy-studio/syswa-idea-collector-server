package cn.saisiawa.ideacollector.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * AI生图任务表
 * </p>
 *
 * @author Saisaiwa
 * @since 2024-07-11
 */
@Getter
@Setter
@TableName("T_AI_COVER_TASK")
public class AiCoverTask {

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
     * 图片尺寸宽
     */
    @TableField("WIDTH")
    private Integer width;

    /**
     * 图片尺寸高
     */
    @TableField("HEIGHT")
    private Integer height;

    /**
     * 使用的模型名称
     */
    @TableField("AI_MODEL")
    private String aiModel;

    /**
     * 反向词
     */
    @TableField("NEGATIVE_PROMPT")
    private String negativePrompt;

    /**
     * 正向提示词
     */
    @TableField("PROMPT")
    private String prompt;

    /**
     * 任务状态：0执行中,1已结束
     */
    @TableField("STATUS")
    private Integer status;

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
