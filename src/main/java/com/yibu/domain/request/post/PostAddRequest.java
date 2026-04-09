package com.yibu.domain.request.post;

import lombok.Data;

import java.io.Serializable;

/**
 * 添加帖子请求
 */
@Data
public class PostAddRequest implements Serializable {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private String tags;

    private static final long serialVersionUID = 1L;
}
