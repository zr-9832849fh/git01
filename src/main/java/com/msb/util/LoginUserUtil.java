package com.msb.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Tony on 2016/8/23.
 * 登录相关
 */
public class LoginUserUtil {

    /**
     * 从cookie中获取userId
     * @param request
     * @return
     */
    public static int releaseUserIdFromCookie(HttpServletRequest request) {
        // 这个key必须和前台保持一致 先获取cookie对象，然后用key拿到value（还没解密的）
        String userIdString = CookieUtil.getCookieValue(request, "userIdStr");  // 这个key必须和前台F12cookie保持一致
        if (StringUtils.isBlank(userIdString)) {
            return 0;
        }
        // 解密
        Integer userId = UserIDBase64.decoderUserID(userIdString);
        return userId;
    }
}
