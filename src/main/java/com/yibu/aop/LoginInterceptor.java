package com.yibu.aop;

import com.yibu.common.ResultCodeEnum;
import com.yibu.constant.UserConstant;
import com.yibu.domain.entity.User;
import com.yibu.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 放行预检请求（重要！！！）
        if ("options".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        // do something...
        User currentUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (currentUser == null) {
            System.out.println("拦截路径：" + request.getRequestURL());
            throw new BusinessException(ResultCodeEnum.NO_LOGIN_ERROR);
        }
        return true;
    }
}