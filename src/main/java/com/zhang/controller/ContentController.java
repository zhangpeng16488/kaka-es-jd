package com.zhang.controller;

import com.zhang.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/parse/{keywords}")
    public boolean parse(@PathVariable("keywords") String keywords){
        boolean b = false;
        try {
            b = contentService.parseContent(keywords);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    @GetMapping("/search/{keywords}/{pageNo}/{pageSize}")
    public List<Map<String,Object>> search(@PathVariable("keywords") String keywords,
                                           @PathVariable("pageNo") int pageNo,
                                           @PathVariable("pageSize") int pageSize){
        List<Map<String,Object>> list = null;
        try {
            list = contentService.searchPageHighlightBuilder(keywords,pageNo,pageSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
