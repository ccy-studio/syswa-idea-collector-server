package cn.saisiawa.ideacollector.domain.req;

import cn.saisiawa.ideacollector.common.bean.BasePageReq;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 15:06
 * @Version：1.0
 */
@Setter
@Getter
public class ArticleCommentReq extends BasePageReq {

    private Long articleId;

}
