package cn.saisiawa.ideacollector.domain.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/12 15:59
 * @Version：1.0
 */
@Data
public class TokenVo {

    private String token;

    private long createTime;

    private long expireTime;

    private Long userId;
}
