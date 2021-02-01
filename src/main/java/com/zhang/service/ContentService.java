package com.zhang.service;

import com.alibaba.fastjson.JSON;
import com.zhang.pojo.Content;
import com.zhang.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.elasticsearch.search.SearchHit;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ContentService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public boolean parseContent(String keywords) throws IOException {
        List<Content> contents = new HtmlParseUtil().parseJD(keywords);
        //把查询的数据放入es中
        BulkRequest request = new BulkRequest();
        request.timeout("2m");
        for (int i = 0; i < contents.size(); i++) {
            request.add(new IndexRequest("jd_goods").source(
                    JSON.toJSONString(contents.get(i)), XContentType.JSON
            ));
        }
        BulkResponse response = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);

        System.out.println(response.status());
        return !response.hasFailures();
    }

    public List<Map<String,Object>> searchPage(String keywords, int pageNo, int pageSize) throws IOException{
        if(pageNo <= 1){
            pageNo = 1;
        }
        //条件搜索
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder builder = new SearchSourceBuilder();

        //分页
        builder.from(pageNo);
        builder.size(pageSize);

        //精准匹配
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("title", keywords);
        builder.query(queryBuilder);
        builder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //执行搜索
        searchRequest.source(builder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        //解析结果
        List<Map<String,Object>> list = new ArrayList<>();
        for(SearchHit documentFields : searchResponse.getHits().getHits()){
            list.add(documentFields.getSourceAsMap());
        }
        return list;
    }
}