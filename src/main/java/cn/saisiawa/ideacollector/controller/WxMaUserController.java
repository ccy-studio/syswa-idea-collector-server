package cn.saisiawa.ideacollector.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.util.WxMaConfigHolder;
import cn.saisiawa.ideacollector.common.bean.BaseResponse;
import cn.saisiawa.ideacollector.common.enums.RespCode;
import cn.saisiawa.ideacollector.domain.vo.TokenVo;
import cn.saisiawa.ideacollector.domain.vo.UserInfoVo;
import cn.saisiawa.ideacollector.service.SessionService;
import cn.saisiawa.ideacollector.service.UserService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户
 *
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/11 16:37
 * @Version：1.0
 */
@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/wx/user")
@Validated
public class WxMaUserController {

    private final WxMaService wxMaService;

    private final UserService userService;

    /**
     * 登陆接口
     */
    @GetMapping("/login")
    public BaseResponse<TokenVo> login(@NotBlank String code) {
        try {
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            return BaseResponse.ok(userService.login(session.getOpenid(), session.getUnionid(), session.getSessionKey()));
        } catch (WxErrorException e) {
            log.error(e.getMessage(), e);
            return BaseResponse.fail(500, "登录失败");
        } finally {
            WxMaConfigHolder.remove();//清理ThreadLocal
        }
    }

    /**
     * 获取用户信息接口，并更新用户基础信息
     */
    @GetMapping("/info")
    public BaseResponse<UserInfoVo> info(String sessionKey,
                                         String signature, String rawData, String encryptedData, String iv) {
        // 用户信息校验
        if (!wxMaService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            WxMaConfigHolder.remove();//清理ThreadLocal
            return BaseResponse.fail(RespCode.INVALID_PARAMS);
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
        userService.updateUserInfo(userInfo);
        WxMaConfigHolder.remove();//清理ThreadLocal
        return getCurrentUser();
    }

    /**
     * 更新用户信息
     *
     * @param avatarUrl
     * @param nickName
     * @return
     */
    @RequestMapping("/update-userinfo")
    public BaseResponse<UserInfoVo> updateUserInfo(@NotBlank String avatarUrl, @NotBlank String nickName) {
        WxMaUserInfo userInfo = new WxMaUserInfo();
        userInfo.setAvatarUrl(avatarUrl);
        userInfo.setNickName(nickName);
        userService.updateUserInfo(userInfo);
        return getCurrentUser();
    }


    /**
     * 获取当前登录的用户信息
     *
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<UserInfoVo> getCurrentUser() {
        return BaseResponse.ok(userService.getUserInfo(SessionService.getSession().getId()));
    }

}
