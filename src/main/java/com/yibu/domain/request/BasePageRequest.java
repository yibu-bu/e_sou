package com.yibu.domain.request;

import com.yibu.constant.CommonConstant;
import lombok.Data;

/**
 * 分页查询基类
 */
@Data
public class BasePageRequest {

    /**
     * 当前页码
     */
    private Integer pageNum = 1;
    /**
     * 单页大小
     */
    private Integer pageSize = 10;
    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
