package com.zhang.utils;

import com.zhang.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class HtmlParseUtil {

//    public static void main(String[] args) throws IOException {
//        new HtmlParseUtil().parseJD("java").forEach(System.out::println);
//    }
    public List<Content> parseJD(String keywords) throws IOException {
        //获取请求 https://search.jd.com/Search?keyword=java
        String url = "https://search.jd.com/Search?keyword=" + keywords;
        //解析网页 (Jsoup返回Document就是浏览器Document对象)
        Document document = Jsoup.parse(new URL(url), 30000);
        //所有在js中可以使用的方法，这里都能用
        Element element = document.getElementById("J_goodsList");
//        System.out.println(element.html());

        List<Content> goodList = new ArrayList<>();

        //获取所有的li
        Elements elements = element.getElementsByTag("li");
        for(Element el : elements){
            //关于这种图片特别多的网站，所有的图片都是延迟加载的
            //data-lazy-img
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String name = el.getElementsByClass("p-name").eq(0).text();
//            System.out.println(img);
//            System.out.println(price);
//            System.out.println(name);
            Content content = new Content();
            content.setImg(img);
            content.setPrice(price);
            content.setTitle(name);
            goodList.add(content);
        }
        return goodList;
    }
}