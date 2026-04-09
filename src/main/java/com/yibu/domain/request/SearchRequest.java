package com.yibu.domain.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchRequest extends BasePageRequest {

    /**
     * 搜索内容
     */
    private String searchText;
    /**
     * 搜索类型
     */
    private String type;
}
