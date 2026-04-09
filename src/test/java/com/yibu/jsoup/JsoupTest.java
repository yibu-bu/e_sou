package com.yibu.jsoup;


import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

/**
 * 网页抓取测试
 */
@SpringBootTest
@Slf4j
public class JsoupTest {

    @Test
    void test01() {
        String url = "https://cn.bing.com/images/search?q=ikun&form=HDRSC2&first=1";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            log.error("网页抓取失败");
        }
        Assertions.assertNotNull(doc);
        Elements imageLinks = doc.select("a.iusc");

        for (Element link : imageLinks) {
            String mAttr = link.attr("m");
            if (mAttr.isEmpty()) continue;
            try {
                Map map = JSONUtil.toBean(mAttr, Map.class);
                String title = map.get("t").toString();
                String murl = map.get("murl").toString();
                System.out.println(title);
                System.out.println(murl);
            } catch (Exception e) {
                System.out.println("解析失败");
            }
        }
    }
}
