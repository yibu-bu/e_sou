package com.yibu.controller;

import com.yibu.common.BaseResponse;
import com.yibu.common.PageResult;
import com.yibu.common.ResultCodeEnum;
import com.yibu.common.ResultUtils;
import com.yibu.domain.entity.Picture;
import com.yibu.domain.request.SearchRequest;
import com.yibu.exception.BusinessException;
import com.yibu.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 获取图片（数据从网上抓取）
     */
    @GetMapping("/list")
    public BaseResponse<PageResult<Picture>> list(SearchRequest searchRequest) {
        if (searchRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // todo 分页
        String searchText = searchRequest.getSearchText();
        Integer pageNum = searchRequest.getPageNum();
        Integer pageSize = searchRequest.getPageSize();
        // 从网络抓取图片数据
        PageResult<Picture> pageResult = pictureService.search(searchText, pageNum, pageSize);
        return ResultUtils.success(pageResult);
    }
}
