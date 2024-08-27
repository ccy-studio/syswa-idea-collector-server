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
 * AI生图结果表
 * </p>
 *
 * @author Saisaiwa
 * @since 2024-07-11
 */
@Getter
@Setter
@TableName("T_AI_COVER_TASK_RESULT")
public class AiCoverTaskResult {

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 任务主表ID
     */
    @TableField("COVER_TASK_ID")
    private Long coverTaskId;

    /**
     * 任务ID
     */
    @TableField("TASK_ID")
    private String taskId;

    /**
     * 预计生成时间/秒
     */
    @TableField("ESTIMATE")
    private Integer estimate;

    /**
     * 临时生成图片地址
     */
    @TableField("GEN_IMG_REMOTE")
    private String genImgRemote;

    /**
     * 保存本机图片路径
     */
    @TableField("GEN_IMG_LOCAL")
    private String genImgLocal;

    /**
     * 任务状态：0执行中,1生成成功,2生成失败
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
