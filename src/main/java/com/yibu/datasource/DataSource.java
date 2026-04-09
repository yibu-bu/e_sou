package com.yibu.datasource;

import com.yibu.common.PageResult;

/**
 * 数据提供接口，其他系统接入提供数据需实现该接口
 */
public interface DataSource<T> {

    /**
     * 获取数据
     *
     * @param searchText 搜索关键词
     * @param pageNum    页码
     * @param pageSize   单页大小
     */
    PageResult<T> getData(String searchText, Integer pageNum, Integer pageSize);
}
