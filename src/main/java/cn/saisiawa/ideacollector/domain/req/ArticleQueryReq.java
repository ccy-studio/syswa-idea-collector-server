package cn.saisiawa.ideacollector.domain.req;

import cn.saisiawa.ideacollector.common.bean.BasePageReq;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/30 13:54
 * @Version：1.0
 */
@Setter
@Getter
public class ArticleQueryReq extends BasePageReq {

    /**
     * 排序条件：0默认综合排序，1点赞排序，2时间排序
     */
    private Integer sortType;

    /**
     * 状态: 0进行中,1已结束
     */
    private Integer status;


    /**
     * 筛选我的文章传入1
     */
    private Integer self;

}
