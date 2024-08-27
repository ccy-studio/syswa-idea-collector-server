package cn.saisiawa.ideacollector.service;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.saisiawa.ideacollector.domain.vo.TokenVo;
import cn.saisiawa.ideacollector.domain.vo.UserInfoVo;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/12 15:52
 * @Version：1.0
 */
public interface UserService {
    TokenVo login(String openId, String unionId,String sessionKey);

    void updateUserInfo(WxMaUserInfo userInfo);

    UserInfoVo getUserInfo(Long id);
}
