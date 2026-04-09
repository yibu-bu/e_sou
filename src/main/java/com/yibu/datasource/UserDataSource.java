package com.yibu.datasource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yibu.common.PageResult;
import com.yibu.common.ResultCodeEnum;
import com.yibu.domain.entity.User;
import com.yibu.exception.BusinessException;
import com.yibu.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDataSource implements DataSource<User> {

    @Resource
    private UserService userService;

    @Override
    public PageResult<User> getData(String searchText, Integer pageNum, Integer pageSize) {
        // 校验请求参数
        if (StringUtils.isEmpty(searchText)) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 分页查询
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
        pageResult.setTotal(userService.count(queryWrapper));   // 总数
        pageResult.setPageNum(pageNum);                  // 当前页码
        pageResult.setPageSize(pageSize);                // 单页大小
        pageResult.setDataList(userList);                // 数据
        return pageResult;
    }
}
