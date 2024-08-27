package cn.saisiawa.ideacollector.domain.req;

import cn.saisiawa.ideacollector.common.group.InsertGroup;
import cn.saisiawa.ideacollector.common.group.UpdateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 9:49
 * @Version：1.0
 */
@Getter
@Setter
public class ArticleCreateReq {

    /**
     * ID
     */
    @NotNull(groups = UpdateGroup.class)
    private Long id;

    /**
     * 标题
     */
    @NotBlank(groups = InsertGroup.class)
    private String title;

    /**
     * 封面
     */
    private String cover;

    /**
     * 内容描述
     */
    @NotBlank
    private String content;

    /**
     * 版本备注
     */
    @NotBlank
    private String comment;


}
