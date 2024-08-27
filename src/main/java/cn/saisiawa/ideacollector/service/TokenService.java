package cn.saisiawa.ideacollector.service;

import cn.saisiawa.ideacollector.domain.entity.User;
import cn.saisiawa.ideacollector.domain.vo.TokenVo;
import cn.saisiawa.ideacollector.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/12 16:09
 * @Version：1.0
 */
@Service
public class TokenService {

    @Resource
    private Redisson redisson;

    @Resource
    private UserMapper userMapper;


    @Value("${expireToken}")
    private long expireToken;


    /**
     * 生成token
     *
     * @param user
     * @return
     */
    public TokenVo generatorToken(User user,String sessionKey) {
        RBucket<Long> bucket = redisson.getBucket(sessionKey);
        Duration duration = Duration.ofSeconds(expireToken);
        bucket.set(user.getId(), duration);

        TokenVo vo = new TokenVo();
        vo.setToken(sessionKey);
        vo.setCreateTime(System.currentTimeMillis());
        vo.setExpireTime(duration.toSeconds());
        vo.setUserId(user.getId());
        return vo;
    }


    /**
     * 根据Token获取用户信息
     *
     * @param token
     * @return
     */
    public User tokenGetUser(String token) {
        RBucket<Long> bucket = redisson.getBucket(token);
        if (!bucket.isExists()) {
            return null;
        }
        bucket.expire(Duration.ofDays(expireToken));
        Long id = bucket.get();
        return userMapper.selectById(id);
    }

}
