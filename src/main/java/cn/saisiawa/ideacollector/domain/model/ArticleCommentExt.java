package cn.saisiawa.ideacollector.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/8/2 14:40
 * @Version：1.0
 */
@Setter
@Getter
public class ArticleCommentExt {

    private Long id;

    /**
     * 用户昵称
     */
    private String userNickName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 点赞数
     */
    private Long give;

    /**
     * 内容
     */
    private String content;

    /**
     * 父评论ID
     */
    private Long parentId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
