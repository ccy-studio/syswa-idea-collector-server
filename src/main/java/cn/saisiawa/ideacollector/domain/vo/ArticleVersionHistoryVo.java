package cn.saisiawa.ideacollector.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 14:12
 * @Version：1.0
 */
@Setter
@Getter
public class ArticleVersionHistoryVo {

    /**
     * 内容描述
     */
    private String content;

    /**
     * 版本备注
     */
    private String comment;

    /**
     * 修改时间
     */
    private String createTime;
}
