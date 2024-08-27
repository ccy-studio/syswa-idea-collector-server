package cn.saisiawa.ideacollector.domain.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 15:00
 * @Version：1.0
 */
@Setter
@Getter
public class ArticleCreateCommentReq {

    /**
     * 文章的ID
     */
    @NotNull
    private Long id;


    /**
     * 评论的内容
     */
    @NotBlank
    private String content;

    /**
     * 父ID
     */
    private Long parentId;

}
