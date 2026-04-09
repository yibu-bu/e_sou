package com.yibu.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果封装类
 */
@Data
public class PageResult<T> implements Serializable {

    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private List<T> dataList;
}
