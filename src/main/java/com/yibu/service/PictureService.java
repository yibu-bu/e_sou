package com.yibu.service;

import com.yibu.common.PageResult;
import com.yibu.domain.entity.Picture;

public interface PictureService {

    PageResult<Picture> search(String searchText, Integer pageNum, Integer pageSize);
}
