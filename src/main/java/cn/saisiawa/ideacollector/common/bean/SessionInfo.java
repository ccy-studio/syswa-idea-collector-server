package cn.saisiawa.ideacollector.common.bean;

import cn.saisiawa.ideacollector.domain.entity.User;
import lombok.Data;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/12 16:31
 * @Version：1.0
 */
@Data
public class SessionInfo {

    private Long id;

    private String openId;

    private User user;

}
