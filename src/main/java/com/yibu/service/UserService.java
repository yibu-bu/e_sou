package com.yibu.service;

import com.yibu.common.PageResult;
import com.yibu.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

    User getSafeUser(User originUser);

    User login(String userAccount, String userPassword);

    long register(String userAccount, String userPassword, String checkPassword);

    User getLoginUser(HttpServletRequest request);

    boolean isAdmin(User loginUser);

    PageResult<User> search(String searchText, Integer pageNum, Integer pageSize);
}
