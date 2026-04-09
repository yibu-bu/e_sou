package com.yibu.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yibu.common.PageResult;
import com.yibu.common.ResultCodeEnum;
import com.yibu.domain.dto.PostEsDTO;
import com.yibu.domain.entity.Post;
import com.yibu.exception.BusinessException;
import com.yibu.mapper.PostMapper;
import com.yibu.service.PostService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
        implements PostService {

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;  // 用于操作 ES 的对象

    @Override
    public void validate(String title, String content, String tags) {
        if (StringUtils.isBlank(title) || title.length() > 50) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "标题不合法");
        }
        if (StringUtils.isBlank(content) || content.length() > 8192) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "内容不合法");
        }
        // 标签允许为空，但必须是合法的 json 数组
        if (StringUtils.isNotBlank(tags) && (!JSONUtil.isTypeJSONArray(tags) || tags.length() > 128)) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "标签不合法");
        }
    }

    @Override
    public PageResult<Post> search(String searchText, Integer pageNum, Integer pageSize) {
        // 校验请求参数
        if (StringUtils.isEmpty(searchText)) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 分页查询
        // 构建查询条件
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", searchText)
                .or().like("content", searchText);
        List<Post> postList = this.list(new Page<>(pageNum, pageSize), queryWrapper);
        // 构建 PageResult 返回对象
        PageResult<Post> pageResult = new PageResult<>();
        pageResult.setTotal(this.count(queryWrapper));   // 总数
        pageResult.setPageNum(pageNum);                  // 当前页码
        pageResult.setPageSize(pageSize);                // 单页大小
        pageResult.setDataList(postList);                // 数据
        return pageResult;
    }

    /**
     * 从 ES 中搜索数据，并结合 MySQL 双查返回完整数据
     *
     * @param searchText 搜索文本
     * @param pageNum    页码
     * @param pageSize   单页大小
     */
    @Override
    @Deprecated
    public PageResult<Post> searchFromES(String searchText, Integer pageNum, Integer pageSize) {
        // 校验请求参数
        if (pageNum <= 0 || pageSize <= 0) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        if (StringUtils.isEmpty(searchText)) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 构建查询条件
        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(searchText, "title", "content", "tags"))
                .withPageable(PageRequest.of(pageNum - 1, pageSize))  // ES页码下标从0开始
                .withSort(Sort.by("createTime").descending())  // 默认按照创建时间倒序
                .build();
        // 查询 ES，提取结果
        SearchHits<PostEsDTO> searchHits = elasticsearchRestTemplate.search(query, PostEsDTO.class);
        List<PostEsDTO> postEsDTOList = searchHits.getSearchHits()
                .stream().map(SearchHit::getContent)
                .collect(Collectors.toList());
        // 双查数据库，获取完整数据
        List<Post> postList = new ArrayList<>();
        for (PostEsDTO postEsDTO : postEsDTOList) {
            Post post = this.getById(postEsDTO.getId());
            // 如果MySQL中没有说明是未及时同步的垃圾数据，直接删掉
            if (post == null) {
                elasticsearchRestTemplate.delete(postEsDTO.getId(), PostEsDTO.class);
            } else {
                postList.add(post);
            }
        }
        // 整合结果
        PageResult<Post> postPageResult = new PageResult<>();
        postPageResult.setTotal(searchHits.getTotalHits());
        postPageResult.setPageNum(pageNum);
        postPageResult.setPageSize(pageSize);
        postPageResult.setDataList(postList);
        // 返回
        return postPageResult;
    }
}




