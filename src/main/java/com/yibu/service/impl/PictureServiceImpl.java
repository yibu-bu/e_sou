package com.yibu.service.impl;

import cn.hutool.json.JSONUtil;
import com.yibu.common.PageResult;
import com.yibu.common.ResultCodeEnum;
import com.yibu.domain.entity.Picture;
import com.yibu.exception.BusinessException;
import com.yibu.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Service
@Slf4j
public class PictureServiceImpl implements PictureService {
    @Override
    public PageResult<Picture> search(String searchText, Integer pageNum, Integer pageSize) {
        if (searchText == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        // todo 分页
        // 从网络抓取图片数据
        String url = String.format("https://cn.bing.com/images/search?q=%s&form=HDRSC2&first=%d", searchText, pageNum);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            log.error("网页抓取失败");
            throw new BusinessException(ResultCodeEnum.SYSTEM_ERROR, "网页抓取失败");
        }
        // 解析图片数据
        ArrayList<Picture> pictures = new ArrayList<>();
        Elements imageLinks = doc.select("a.iusc");
        for (Element link : imageLinks) {
            String mAttr = link.attr("m");
            if (mAttr.isEmpty()) continue;
            try {
                Map map = JSONUtil.toBean(mAttr, Map.class);
                String title = map.get("t").toString();
                String murl = map.get("murl").toString();
                if (StringUtils.isAnyBlank(title, murl)) {
                    continue;
                }
                if (pictures.size() >= pageSize) {  // 限制数量
                    break;
                }
                pictures.add(new Picture(title, murl));
            } catch (Exception e) {
                System.out.println("解析失败");
            }
        }
        // 构建返回对象
        PageResult<Picture> pageResult = new PageResult<>();
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        pageResult.setDataList(pictures);
        return pageResult;
    }
}
