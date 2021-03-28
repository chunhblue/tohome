package com.nri.esAPI.Util;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

public class esUtil {
    //private static final Logger LOGGER = LoggerFactory.getLogger(EsUtil.class);
    /**
     * 获取client连接
     */
    public  static RestHighLevelClient getHighLevelClient(String host,int port,String schema) {
       /* //单个ip创建方式
        HttpHost  httpHost= new HttpHost(host, port, schema);
        RestClientBuilder builder = RestClient.builder(httpHost);
        RestHighLevelClient  client = new RestHighLevelClient(builder);
        return client;*/
        String[] ipArr = host.split(",");
        HttpHost[] httpHosts = new HttpHost[ipArr.length];
        for (int i = 0; i < ipArr.length; i++) {
            httpHosts[i] = new HttpHost(ipArr[i], port, schema);
        }
        RestClientBuilder builder = RestClient.builder(httpHosts);
        RestHighLevelClient  client = new RestHighLevelClient(builder);
        return  client;
    }
    
    /**
     * 关闭连接
     */
    public static void close(RestHighLevelClient client) {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}