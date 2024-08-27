package cn.saisiawa.ideacollector.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.saisiawa.ideacollector.domain.entity.User;
import cn.saisiawa.ideacollector.domain.vo.TokenVo;
import cn.saisiawa.ideacollector.domain.vo.UserInfoVo;
import cn.saisiawa.ideacollector.mapper.UserMapper;
import cn.saisiawa.ideacollector.service.SessionService;
import cn.saisiawa.ideacollector.service.TokenService;
import cn.saisiawa.ideacollector.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/12 15:52
 * @Version：1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenService tokenService;


    /**
     * 登录
     *
     * @param openId  openid
     * @param unionId unionId
     * @return {@link TokenVo}
     */
    @Override
    public TokenVo login(String openId, String unionId, String sessionKey) {
        User user = userMapper.getByOpenId(openId);
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
            user.setUnionId(unionId);
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);
        }
        return tokenService.generatorToken(user,sessionKey);
    }


    /**
     * 更新用户信息
     *
     * @param userInfo
     */
    @Override
    public void updateUserInfo(WxMaUserInfo userInfo) {
        User user = userMapper.selectById(SessionService.getSession().getId());
        user.setNickName(userInfo.getNickName());
        user.setAvatar(userInfo.getAvatarUrl());
        userMapper.updateById(user);
    }


    /**
     * 返回用户信息
     *
     * @param id
     * @return
     */
    @Override
    public UserInfoVo getUserInfo(Long id) {
        User user = userMapper.selectById(id);
        Assert.notNull(user);
        return BeanUtil.copyProperties(user, UserInfoVo.class);
    }

}
