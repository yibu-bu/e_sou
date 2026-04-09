package com.yibu.datasource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据源注册器（注册器模式）
 */
@Component
public class DataSourceRegistry {

    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PostDataSource postDataSource;
    @Resource
    private PictureDataSource pictureDataSource;

    private Map<String, DataSource<?>> dataSourceMap;

    @PostConstruct
    public void init() {
        dataSourceMap = new HashMap<String, DataSource<?>>() {{
            put("user", userDataSource);
            put("post", postDataSource);
            put("picture", pictureDataSource);
        }};
    }

    /**
     * 根据类型获取数据源对象
     */
    public DataSource<?> getDataSourceByType(String type) {
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        return dataSourceMap.get(type);
    }
}
