package com.yibu.aop;

import com.yibu.annotation.RequireAdmin;
import com.yibu.common.ResultCodeEnum;
import com.yibu.constant.UserRoleEnum;
import com.yibu.domain.entity.User;
import com.yibu.exception.BusinessException;
import com.yibu.service.UserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    /**
     * 执行拦截
     */
    @Around("@annotation(requireAdmin)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, RequireAdmin requireAdmin) throws Throwable {
        // 注解被设为false，直接放行
        if (!requireAdmin.require()) {
            return joinPoint.proceed();
        }
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        // 获取当前登录用户及身份
        User loginUser = userService.getLoginUser(request);
        String userRole = loginUser.getUserRole();
        if (!userRole.equals(UserRoleEnum.ADMIN.getValue())) {
            throw new BusinessException(ResultCodeEnum.NO_AUTH_ERROR);
        }
        // 放行
        return joinPoint.proceed();
    }
}