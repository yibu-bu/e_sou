package com.yibu.service;

import com.yibu.common.PageResult;
import com.yibu.domain.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PostService extends IService<Post> {

    void validate(String title, String content, String tags);

    PageResult<Post> search(String searchText, Integer pageNum, Integer pageSize);

    PageResult<Post> searchFromES(String searchText, Integer pageNum, Integer pageSize);
}
