package top.openfbi.mdnote.user.util;

import jakarta.servlet.http.HttpSession;
import top.openfbi.mdnote.config.ResponseResultBody;
import top.openfbi.mdnote.user.model.UserSession;

/**
 * 用户登录操作类
 */
@ResponseResultBody
public class Session {
    // session保存在Redis中的key
    static String USER_SESSION_KEY = "user";

    private HttpSession httpSession;

    /**
     * 获取用户Session
     */
    public Session() {
        // 获取当前链接session
        httpSession = SessionUtil.getHttpSession();
    }

    /**
     * 设置当前连接的HttpSession
     */
    public Session(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    /**
     * 设置用户登录Session
     * session的key为USER_SESSION_KEY
     */
    public static void setUser(UserSession userSession) {
        new Session().httpSession.setAttribute(USER_SESSION_KEY, userSession);
    }

    /**
     * 获取当前登录用户信息
     */
    public static UserSession getUser() {
        //根据当前链接cookie中保存的sessionId和USER_SESSION_KEY获取当前连接的用户信息
        return (UserSession) new Session().httpSession.getAttribute(USER_SESSION_KEY);
    }

    /**
     * 删除Session，退出登录状态
     */
    public void invalidate() {
        httpSession.invalidate();
    }
}