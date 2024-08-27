package cn.saisiawa.ideacollector.service;

import cn.saisiawa.ideacollector.common.bean.SessionInfo;
import cn.saisiawa.ideacollector.common.enums.RespCode;
import cn.saisiawa.ideacollector.common.exception.BizException;

/**
 * @Description:
 * @Author: Chen Ze Deng
 * @Date: 2024/7/12 16:30
 * @Version：1.0
 */
public class SessionService {

    private static final ThreadLocal<SessionInfo> THREAD_LOCAL = new InheritableThreadLocal<>();


    public static void setSession(SessionInfo session) {
        THREAD_LOCAL.set(session);
    }

    public static SessionInfo getSessionOrNull() {
        return THREAD_LOCAL.get();
    }

    public static SessionInfo getSession() {
        SessionInfo sessionInfo = getSessionOrNull();
        if (sessionInfo == null) {
            throw new BizException(RespCode.SESSION_TIMEOUT);
        }
        return sessionInfo;
    }


    public static void clear() {
        THREAD_LOCAL.remove();
    }

}
