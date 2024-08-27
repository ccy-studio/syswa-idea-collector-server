package cn.saisiawa.ideacollector.conf;

import cn.hutool.core.util.StrUtil;
import cn.saisiawa.ideacollector.common.anno.SessionCheck;
import cn.saisiawa.ideacollector.common.bean.SessionInfo;
import cn.saisiawa.ideacollector.common.enums.RespCode;
import cn.saisiawa.ideacollector.common.exception.BizException;
import cn.saisiawa.ideacollector.domain.entity.User;
import cn.saisiawa.ideacollector.service.SessionService;
import cn.saisiawa.ideacollector.service.TokenService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/3/6 17:54
 * @Version：1.0
 */
@Component
public class SysSessionInterceptor implements HandlerInterceptor {

    @Resource
    private TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest httpReq, HttpServletResponse response, Object handler) throws Exception {
        boolean ignoreSession = this.ignoreHandle(handler);
        String authorization = httpReq.getHeader("Authorization");
        if (StrUtil.isEmpty(authorization)) {
            // 需要登录态，但没有传Token
            if (!ignoreSession) {
                throw new BizException(RespCode.UNAUTHORIZED);
            }
        } else {
            User user = tokenService.tokenGetUser(authorization);
            if (user == null) {
                throw new BizException(RespCode.INVALID_TOKEN);
            }
            SessionInfo sessionInfo = new SessionInfo();
            sessionInfo.setUser(user);
            sessionInfo.setId(user.getId());
            sessionInfo.setOpenId(user.getOpenId());
            SessionService.setSession(sessionInfo);
        }
        return true;
    }

    private boolean ignoreHandle(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            SessionCheck sessionCheck = method.getMethodAnnotation(SessionCheck.class);
            return (sessionCheck != null && sessionCheck.ignore());
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SessionService.clear();
    }
}
