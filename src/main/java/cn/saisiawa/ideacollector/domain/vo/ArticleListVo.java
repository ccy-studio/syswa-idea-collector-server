package cn.saisiawa.ideacollector.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 11:43
 * @Version：1.0
 */
@Setter
@Getter
public class ArticleListVo {

    private Long id;


    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userNickName;

    /**
     * 用户头像
     */
    private String userAvatar;


    /**
     * 点赞数
     */
    private Long give;

    /**
     * 阅读数
     */
    private Long see;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 状态: 0进行中,1已结束
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date createTime;
}
