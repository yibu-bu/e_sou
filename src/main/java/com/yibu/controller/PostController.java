package com.yibu.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yibu.common.BaseResponse;
import com.yibu.common.PageResult;
import com.yibu.common.ResultCodeEnum;
import com.yibu.common.ResultUtils;
import com.yibu.domain.entity.Post;
import com.yibu.domain.entity.User;
import com.yibu.domain.request.SearchRequest;
import com.yibu.domain.request.post.PostAddRequest;
import com.yibu.domain.request.post.PostUpdateRequest;
import com.yibu.exception.BusinessException;
import com.yibu.service.PostService;
import com.yibu.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    /**
     * 添加帖子
     */
    @PostMapping("/add")
    public BaseResponse<Long> add(@RequestBody PostAddRequest postAddRequest, HttpServletRequest request) {
        // 校验请求参数
        if (postAddRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 校验
        String title = postAddRequest.getTitle();
        String content = postAddRequest.getContent();
        String tags = postAddRequest.getTags();
        postService.validate(title, content, tags);  // 校验请求参数合法性
        Post post = new Post();
        BeanUtils.copyProperties(postAddRequest, post);
        post.setUserId(loginUser.getId());
        boolean save = postService.save(post);
        if (!save) {
            throw new BusinessException(ResultCodeEnum.OPERATION_ERROR, "添加失败");
        }
        // 返回
        return ResultUtils.success(post.getId());
    }

    /**
     * 分页获取帖子列表
     */
    @GetMapping("/list")
    public BaseResponse<PageResult<Post>> list(SearchRequest searchRequest) {
        // 校验请求参数
        if (searchRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 分页查询
        Integer pageNum = searchRequest.getPageNum();
        Integer pageSize = searchRequest.getPageSize();
        String searchText = searchRequest.getSearchText();
        // 构建查询条件
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", searchText)
                .or().like("content", searchText);
        List<Post> postList = postService.list(new Page<>(pageNum, pageSize), queryWrapper);
        // 构建 PageResult 返回对象
        PageResult<Post> pageResult = new PageResult<>();
        pageResult.setTotal(postService.count());   // 总数
        pageResult.setPageNum(pageNum);             // 当前页码
        pageResult.setPageSize(pageSize);           // 单页大小
        pageResult.setDataList(postList);           // 数据
        return ResultUtils.success(pageResult);
    }

    /**
     * 删除帖子
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 查询帖子
        Post oldPost = postService.getById(id);
        if (oldPost == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND_ERROR);
        }
        // 鉴权
        long userId = oldPost.getUserId();
        User loginUser = userService.getLoginUser(request);
        if (!userService.isAdmin(loginUser) && !loginUser.getId().equals(userId)) {
            throw new BusinessException(ResultCodeEnum.NO_AUTH_ERROR);
        }
        // 删除
        return ResultUtils.success(postService.removeById(id));
    }

    /**
     * 更新帖子
     */
    @PostMapping("/update")
    public BaseResponse<Post> update(@RequestBody PostUpdateRequest postUpdateRequest, HttpServletRequest request) {
        // 校验请求不为null
        if (postUpdateRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 校验参数合法性
        Long id = postUpdateRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        Post oldPost = postService.getById(id);
        if (oldPost == null) {
            throw new BusinessException(ResultCodeEnum.NOT_FOUND_ERROR);
        }
        // 鉴权
        User loginUser = userService.getLoginUser(request);
        if (!userService.isAdmin(loginUser) && !loginUser.getId().equals(oldPost.getUserId())) {
            throw new BusinessException(ResultCodeEnum.NO_AUTH_ERROR);
        }
        // 校验（防御性编程，防止前端没有传全量数据）
        String title = postUpdateRequest.getTitle();
        if (StringUtils.isNotBlank(title)) {
            if (title.length() > 50) {
                throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "标题过长");
            } else {
                oldPost.setTitle(title);
            }
        }
        String content = postUpdateRequest.getContent();
        if (StringUtils.isNotBlank(content)) {
            if (content.length() > 8192) {
                throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "内容过长");
            } else {
                oldPost.setContent(content);
            }
        }
        String tags = postUpdateRequest.getTags();
        if (StringUtils.isNotBlank(tags)) {
            if (!JSONUtil.isTypeJSONArray(tags) || tags.length() > 128) {
                throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "标签不合法");
            } else {
                oldPost.setTags(tags);
            }
        }
        // 更新、返回
        boolean update = postService.updateById(oldPost);
        if (!update) {
            throw new BusinessException(ResultCodeEnum.OPERATION_ERROR, "更新失败");
        }
        return ResultUtils.success(oldPost);
    }
}
