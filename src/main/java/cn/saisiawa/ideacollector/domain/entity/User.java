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
 * 用户表
 * </p>
 *
 * @author Saisaiwa
 * @since 2024-07-11
 */
@Getter
@Setter
@TableName("T_USER")
public class User {

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * OpenId
     */
    @TableField("OPEN_ID")
    private String openId;

    /**
     * UnionID
     */
    @TableField("UNION_ID")
    private String unionId;

    /**
     * 用户昵称
     */
    @TableField("NICK_NAME")
    private String nickName;

    /**
     * 头像
     */
    @TableField("AVATAR")
    private String avatar;

    /**
     * 城市
     */
    @TableField("CITY")
    private String city;

    /**
     * 省份
     */
    @TableField("PROVINCE")
    private String province;

    /**
     * 性别 1男,0女
     */
    @TableField("GENDER")
    private Integer gender;

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
