package com.yibu.domain.vo;

import com.yibu.common.PageResult;
import com.yibu.domain.entity.Picture;
import com.yibu.domain.entity.Post;
import com.yibu.domain.entity.User;
import lombok.Data;

/**
 * 聚合搜索结果
 */
@Data
public class SearchResultVO {

    private PageResult<User> userPageResult;
    private PageResult<Post> postPageResult;
    private PageResult<Picture> picturePageResult;

    /**
     * 不确定类型，当以上三个字段都为 null 时，前端可以尝试解析这个类型
     */
    private PageResult<?> dataPageResult;
}
