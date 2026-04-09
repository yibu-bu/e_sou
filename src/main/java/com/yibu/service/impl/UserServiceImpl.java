package com.yibu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibu.common.PageResult;
import com.yibu.common.ResultCodeEnum;
import com.yibu.constant.UserConstant;
import com.yibu.constant.UserRoleEnum;
import com.yibu.domain.entity.User;
import com.yibu.exception.BusinessException;
import com.yibu.mapper.UserMapper;
import com.yibu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 用户脱敏
     */
    @Override
    public User getSafeUser(User originUser) {
        originUser.setUserPassword(null);
        originUser.setIsDelete(null);
        return originUser;
    }

    /**
     * 登录
     */
    @Override
    public User login(String userAccount, String userPassword) {
        // 校验参数不为null
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 校验（减少查数据库）
        if (userAccount.length() < 4 || userPassword.length() < 6) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND_ERROR, "用户名密码不匹配");
        }
        // 查询数据库
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.getOne(queryWrapper);
        if (user == null) {
            log.info("login user not exist");
            throw new BusinessException(ResultCodeEnum.NOT_FOUND_ERROR, "用户名密码不匹配");
        }
        // 如果被封禁，直接拒绝
        if (user.getUserRole().equals(UserRoleEnum.BAN.getValue())) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND_ERROR, "账号被封禁");
        }
        // 返回脱敏用户
        return getSafeUser(user);
    }

    /**
     * 注册
     *
     * @return userId
     */
    @Override
    public long register(String userAccount, String userPassword, String checkPassword) {
        // 校验请求参数不为null
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 校验用户名、密码合法性
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "用户名长度不能小于4");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND_ERROR, "两次输入的密码不一致");
        }
        if (userPassword.length() < 6) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "密码长度不能小于6");
        }
        // 插入数据库
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserRole("user");
        try {
            this.save(user);
        } catch (DuplicateKeyException e) {  // 违反数据库唯一性索引
            log.info("register user already exist");
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "用户名已存在");
        }
        return user.getId();
    }

    /**
     * 获取当前登录用户
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ResultCodeEnum.NO_LOGIN_ERROR);
        }
        return loginUser;
    }

    /**
     * 判断用户是否为管理员
     */
    @Override
    public boolean isAdmin(User loginUser) {
        if (loginUser == null) {
            return false;
        }
        return loginUser.getUserRole().equals(UserRoleEnum.ADMIN.getValue());
    }

    /**
     * 搜索用户
     */
    @Override
    public PageResult<User> search(String searchText, Integer pageNum, Integer pageSize) {
        // 校验请求参数
        if (StringUtils.isEmpty(searchText)) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 分页查询
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("userName", searchText)
                .or().like("userProfile", searchText);
        List<User> userList = this.list(new Page<>(pageNum, pageSize), queryWrapper)
                .stream()
                .map(this::getSafeUser)  // 脱敏
                .collect(Collectors.toList());
        // 构建 PageResult 返回对象
        PageResult<User> pageResult = new PageResult<>();
        pageResult.setTotal(this.count(queryWrapper));   // 总数
        pageResult.setPageNum(pageNum);                  // 当前页码
        pageResult.setPageSize(pageSize);                // 单页大小
        pageResult.setDataList(userList);                // 数据
        return pageResult;
    }
}




