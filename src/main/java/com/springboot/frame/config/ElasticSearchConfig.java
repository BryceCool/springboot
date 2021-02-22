package com.springboot.frame.config;

import com.springboot.frame.constant.Constant;
import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * ElasticSearch 配置类
 *
 * @author yangguanghui6
 * @date 2021/2/7 20:17
 */
@Configuration
@Data
public class ElasticSearchConfig {

    private Logger logger = LoggerFactory.getLogger(ElasticSearchConfig.class);

    /**
     * 协议
     */
    @Value("{elasticsearch.config.schema:http}")
    private String schema;

    /**
     * 集群地址，如果有多个用“,”隔开
     */
    @Value("{elasticsearch.config.address}")
    private String address;

    /**
     * 连接超时时间
     */
    @Value("{elasticsearch.config.connectTimeOut:5000}")
    private int connectTimeOut;

    /**
     * Socket 连接超时时间
     */
    @Value("{elasticsearch.config.socketTimeOut:5000}")
    private int socketTimeOut;

    /**
     * 获取连接的超时时间
     */
    @Value("{elasticsearch.config.connectionRequestTimeOut:5000}")
    private int connectionRequestTimeOut;

    /**
     * 最大连接数
     */
    @Value("{elasticsearch.config.maxConnectNum:100}")
    private int maxConnectNum;

    /**
     * 最大路由连接数
     */
    @Value("{elasticsearch.config.maxConnectPerRoute:100}")
    private int maxConnectPerRoute;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        RestClientBuilder builder = null;
        List<HttpHost> hostLists = new ArrayList<>();
        // 拆分Es 地址
        if (this.address != null) {
            String[] addressList = this.address.split(Constant.SPLIT_EN_COMMA);
            for (String address : addressList) {
                String[] hostPortInfo = address.split(Constant.SPLIT_EN_COLON);
                String host = hostPortInfo[0];
                String port = hostPortInfo[1];
                hostLists.add(new HttpHost(host, Integer.parseInt(port), schema));
            }
            // 转换成 HttpHost 数组
            HttpHost[] httpHosts = hostLists.toArray(new HttpHost[]{});
            // 构建连接对象
            builder = RestClient.builder(httpHosts);
            // 异步连接延时配置
            builder.setRequestConfigCallback(requestConfigBuilder -> {
                requestConfigBuilder.setConnectTimeout(connectTimeOut);
                requestConfigBuilder.setSocketTimeout(socketTimeOut);
                requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
                return requestConfigBuilder;
            });
            // 异步连接数配置
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                httpClientBuilder.setMaxConnTotal(maxConnectNum);
                httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
                return httpClientBuilder;
            });
        } else {
            logger.error("ElasticSearch Host Has Not Config!Please Check it!");
        }
        return new RestHighLevelClient(builder);
    }

}