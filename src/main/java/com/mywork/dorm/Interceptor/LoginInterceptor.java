package com.mywork.dorm.Interceptor;


import com.mywork.dorm.Utils.JwtUtil;
import com.mywork.dorm.Utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 得到令牌验证
        String token = request.getHeader("Authorization");
        try {
            // 验证token是否被篡改👇

//            1 把token存入本线程内的‘公共区域’
            Map<String, Object> map = JwtUtil.parseToken(token);
            // 以Aop的思想完成全局获取token，不需要单独调用
            ThreadLocalUtil.set(map);
            return true;
        }catch (Exception e){
            // 如果被篡改就发送401到前端
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        线程返回时销毁
        ThreadLocalUtil.remove();
    }
}
