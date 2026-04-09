package com.yibu.controller;

import com.yibu.common.BaseResponse;
import com.yibu.common.ResultCodeEnum;
import com.yibu.common.ResultUtils;
import com.yibu.domain.request.SearchRequest;
import com.yibu.domain.vo.SearchResultVO;
import com.yibu.facade.SearchFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * 聚合搜索，统一搜索各种类型的数据
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Resource
    private SearchFacade searchFacade;

    /**
     * 聚合搜索
     */
    @GetMapping("/searchAll")
    public BaseResponse<SearchResultVO> search(SearchRequest searchRequest) throws ExecutionException, InterruptedException {
        // 校验请求参数
        if (searchRequest == null) {
            return ResultUtils.error(ResultCodeEnum.PARAMS_ERROR);
        }
        String searchText = searchRequest.getSearchText();
        String type = searchRequest.getType();
        Integer pageNum = searchRequest.getPageNum();
        Integer pageSize = searchRequest.getPageSize();
        SearchResultVO searchResultVO = searchFacade.search(searchText, pageNum, pageSize, type);
        return ResultUtils.success(searchResultVO);
    }
}
