package cn.saisiawa.ideacollector.domain.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/12 16:52
 * @Version：1.0
 */
@Data
public class UserInfoVo {

    /**
     * ID
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 城市
     */
    private String city;

    /**
     * 省份
     */
    private String province;

    /**
     * 性别 1男,0女
     */
    private Integer gender;


}
