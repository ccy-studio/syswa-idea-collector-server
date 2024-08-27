package cn.saisiawa.ideacollector.mapper;

import cn.saisiawa.ideacollector.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Saisaiwa
 * @since 2024-07-11
 */
public interface UserMapper extends BaseMapper<User> {


    User getByOpenId(String openId);

    boolean existOpenId(String openId);

}
