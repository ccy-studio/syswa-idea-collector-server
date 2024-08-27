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
 * 创意主表
 * </p>
 *
 * @author Saisaiwa
 * @since 2024-07-11
 */
@Getter
@Setter
@TableName("T_ARTICLE")
public class Article {

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 作者
     */
    @TableField("USER_ID")
    private Long userId;

    /**
     * 点赞数
     */
    @TableField("GIVE")
    private Long give;

    /**
     * 阅读数
     */
    @TableField("SEE")
    private Long see;

    /**
     * 标题
     */
    @TableField("TITLE")
    private String title;

    /**
     * 封面
     */
    @TableField("COVER")
    private String cover;

    /**
     * 状态: 0进行中,1已结束
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
