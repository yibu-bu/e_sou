package com.yibu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yibu.annotation.RequireAdmin;
import com.yibu.common.BaseResponse;
import com.yibu.common.PageResult;
import com.yibu.common.ResultCodeEnum;
import com.yibu.common.ResultUtils;
import com.yibu.constant.UserConstant;
import com.yibu.domain.entity.User;
import com.yibu.domain.request.SearchRequest;
import com.yibu.domain.request.user.UserLoginRequest;
import com.yibu.domain.request.user.UserRegisterRequest;
import com.yibu.exception.BusinessException;
import com.yibu.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 校验请求参数不为null
        if (userLoginRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 调用业务层方法
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        User loginUser = userService.login(userAccount, userPassword);
        // 保存登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, loginUser);
        // 返回
        return ResultUtils.success(loginUser);
    }

    @GetMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return ResultUtils.success(true);
    }

    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 校验请求参数不为null
        if (userRegisterRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 调用业务层方法
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        Long userId = userService.register(userAccount, userPassword, checkPassword);
        // 返回
        return ResultUtils.success(userId);
    }

    /**
     * 分页获取用户列表
     */
    @GetMapping("/list")
    @RequireAdmin
    public BaseResponse<PageResult<User>> list(SearchRequest searchRequest) {
        // 校验请求参数
        if (searchRequest == null) {
            return ResultUtils.error(ResultCodeEnum.PARAMS_ERROR);
        }
        // 分页查询
        Integer pageNum = searchRequest.getPageNum();
        Integer pageSize = searchRequest.getPageSize();
        String searchText = searchRequest.getSearchText();
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("userName", searchText)
                .or().like("userProfile", searchText);
        List<User> userList = userService.list(new Page<>(pageNum, pageSize), queryWrapper)
                .stream()
                .map(userService::getSafeUser)  // 脱敏
                .collect(Collectors.toList());
        // 构建 PageResult 返回对象
        PageResult<User> pageResult = new PageResult<>();
        pageResult.setTotal(userService.count());   // 总数
        pageResult.setPageNum(pageNum);             // 当前页码
        pageResult.setPageSize(pageSize);           // 单页大小
        pageResult.setDataList(userList);           // 数据
        return ResultUtils.success(pageResult);
    }
}
