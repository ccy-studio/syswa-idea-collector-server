package cn.saisiawa.ideacollector.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 11:47
 * @Version：1.0
 */
@Setter
@Getter
public class ArticleCommentVo {

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
     * 父评论的用户昵称
     */
    private String parentUserName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 子评论
     */
    private List<ArticleCommentVo> childComment;

}
