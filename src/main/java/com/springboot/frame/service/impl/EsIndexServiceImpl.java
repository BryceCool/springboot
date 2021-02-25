package com.springboot.frame.service.impl;

import com.springboot.frame.service.EsIndexService;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author yangguanghui6
 * Es 创建索引service
 */
@Service
public class EsIndexServiceImpl implements EsIndexService {

    private Logger logger = LoggerFactory.getLogger(EsIndexServiceImpl.class);

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 创建索引
     */
    @Override
    public void createIndex() {

        try {
            // 创建mapping ： 通过XContentFactory的json方式来创建
            XContentBuilder mapping = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("dynamic", true)
                    .startObject("properties")
                    .startObject("name")
                    .field("type", "text")
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type", "keyword")
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("address")
                    .field("type", "text")
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type", "keyword")
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("remark")
                    .field("type", "text")
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type", "keyword")
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("age")
                    .field("type", "integer")
                    .endObject()
                    .startObject("salary")
                    .field("type", "float")
                    .endObject()
                    .startObject("birthDate")
                    .field("type", "date")
                    .field("format", "yyyy-MM-dd")
                    .endObject()
                    .startObject("createTime")
                    .field("type", "date")
                    .endObject()
                    .endObject()
                    .endObject();
            // 创建Settings : 通过Settings.build来创建索引配置信息
            Settings settings = Settings.builder()
                    .put("index.number_of_shards", 1)
                    .put("index.number_of_replicas", 1)
                    .build();
            // 新建创建索引请求对象，然后设置索引类型、mapping、settings
            CreateIndexRequest request = new CreateIndexRequest("mydlq-user");
            request.mapping(mapping);
            request.settings(settings);
            // RestHighLevelClient 执行创建索引
            CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            // 判断是否创建成功
            boolean createFlag = response.isAcknowledged();
            logger.info("是否创建成功：{}", createFlag);
        } catch (IOException e) {
            logger.error("未成功创建索引！");
        }
    }

    /**
     * 删除索引：这里为了简便，未对返回做实体类封装
     */
    @Override
    public void deleteIndex() {
        try {
            // 新建删除索引请求对象
            DeleteIndexRequest request = new DeleteIndexRequest("mydlq-user");

            // RestHighLevelClient 执行删除索引
            AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
            // 判断是否删除成功
            boolean acknowledged = acknowledgedResponse.isAcknowledged();
            logger.info("是否删除成功：{}", acknowledged);
        } catch (IOException e) {
            logger.error("删除索引失败！");
        }

    }


}
