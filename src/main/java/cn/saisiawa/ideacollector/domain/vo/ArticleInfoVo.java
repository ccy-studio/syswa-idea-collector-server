package cn.saisiawa.ideacollector.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 11:45
 * @Version：1.0
 */
@Setter
@Getter
public class ArticleInfoVo extends ArticleListVo {

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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private Date updateTime;

}
