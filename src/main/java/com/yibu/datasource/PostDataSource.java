package com.yibu.datasource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yibu.common.PageResult;
import com.yibu.common.ResultCodeEnum;
import com.yibu.domain.dto.PostEsDTO;
import com.yibu.domain.entity.Post;
import com.yibu.exception.BusinessException;
import com.yibu.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PostDataSource implements DataSource<Post> {

    @Resource
    private PostService postService;
    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 实现方法为从ES中模糊搜索
     */
    @Override
    public PageResult<Post> getData(String searchText, Integer pageNum, Integer pageSize) {
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
//                .withSort(Sort.by("createTime").descending())  // 按照创建时间倒序
                // 不指定的话默认会按照 _score 排序（相关性越高越靠前）
                .build();
        // 查询 ES，提取结果
        SearchHits<PostEsDTO> searchHits = elasticsearchRestTemplate.search(query, PostEsDTO.class);
        List<PostEsDTO> postEsDTOList = searchHits.getSearchHits()
                .stream().map(SearchHit::getContent)
                .collect(Collectors.toList());
        // 双查数据库，获取完整数据
        List<Post> postList = new ArrayList<>();
        for (PostEsDTO postEsDTO : postEsDTOList) {
            Post post = postService.getById(postEsDTO.getId());
            // 如果MySQL中没有说明是未及时同步的垃圾数据，直接删掉
            if (post == null) {
                elasticsearchRestTemplate.delete(postEsDTO.getId(), PostEsDTO.class);
                log.info("删除垃圾数据：{}", postEsDTO);
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

    /**
     * 从数据库中查询数据
     */
    public PageResult<Post> getDataFromDB(String searchText, Integer pageNum, Integer pageSize) {
        // 校验请求参数
        if (StringUtils.isEmpty(searchText)) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // 分页查询
        // 构建查询条件
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", searchText)
                .or().like("content", searchText);
        List<Post> postList = postService.list(new Page<>(pageNum, pageSize), queryWrapper);
        // 构建 PageResult 返回对象
        PageResult<Post> pageResult = new PageResult<>();
        pageResult.setTotal(postService.count(queryWrapper));   // 总数
        pageResult.setPageNum(pageNum);                         // 当前页码
        pageResult.setPageSize(pageSize);                       // 单页大小
        pageResult.setDataList(postList);                       // 数据
        return pageResult;
    }
}
