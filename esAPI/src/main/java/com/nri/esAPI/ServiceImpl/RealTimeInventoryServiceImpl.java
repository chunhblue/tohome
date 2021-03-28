package com.nri.esAPI.ServiceImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Cardinality;
import org.elasticsearch.search.aggregations.metrics.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.InternalTopHits;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.nri.esAPI.DTO.RealTimeDto;
import com.nri.esAPI.Service.RealTimeInventoryService;
import com.nri.esAPI.Util.esUtil;

import com.nri.esAPI.Util.Util;


@Repository
public class RealTimeInventoryServiceImpl implements RealTimeInventoryService{

	@Override
	public String GetRelTimeInventory(String StoreCd,String articleId,String topDepartment,
	        String department,String category,String subCategory,String inEsTime,
	        String page, String size,String articleIdListJson) throws IOException, ParseException {
	    
		String host = Util.getProperty("es.nodes");
        int port = Integer.valueOf(Util.getProperty("es.port"));
        String schema = "http";
		RestHighLevelClient client= esUtil.getHighLevelClient(host, port, schema);
		
		Map<String, Object> mapLast=new HashMap<String, Object>();
		
		// 1、创建search请求
	    //SearchRequest searchRequest = new SearchRequest();
	    SearchRequest searchRequest = new SearchRequest("tran_detail");
	 
	    // 2、用SearchSourceBuilder来构造查询请求体 ,请仔细查看它的方法，构造各种查询的方法都在这。
	    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
	 
	    //构造QueryBuilder
	    BoolQueryBuilder matchQueryBuilder = QueryBuilders.boolQuery(); 
	    //QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("store_cd", StoreCd);
	    matchQueryBuilder.must(QueryBuilders.matchPhraseQuery("store_cd", StoreCd));
	    List<String> articleIdList = new ArrayList<String>();
	    if (articleIdListJson != null && articleIdListJson != "") {
	        try {
	            articleIdList = JSON.parseArray(articleIdListJson, String.class);
	        } catch (Exception e) {
	            mapLast.put("status", "4");
	            mapLast.put("message", "The articleIdList Json Data Error!");
	            client.close();
	            String jsonLast=JSON.toJSONString(mapLast);
	            return jsonLast;
	        }
	        matchQueryBuilder.must(QueryBuilders.termsQuery("article_id", articleIdList));
        }else {
            if(articleId!=null && !articleId.equals("")&& !articleId.equals("*")) {
                matchQueryBuilder.must(QueryBuilders.wildcardQuery("article_id", "*"+articleId+"*"));
            }
        }
	    if(topDepartment!=null && !topDepartment.equals("")&& !topDepartment.equals("*")) {
	        matchQueryBuilder.must(QueryBuilders.matchPhraseQuery("dep_cd", topDepartment));
	    }
	    if(department!=null && !department.equals("")&& !department.equals("*")) {
	        matchQueryBuilder.must(QueryBuilders.matchPhraseQuery("pma_cd", department));
	    }
	    if(category!=null && !category.equals("")&& !category.equals("*")) {
	        matchQueryBuilder.must(QueryBuilders.matchPhraseQuery("category_cd", category));
	    }
	    if(subCategory!=null && !subCategory.equals("")&& !subCategory.equals("*")) {
	        matchQueryBuilder.must(QueryBuilders.matchPhraseQuery("sub_category_cd", subCategory));
	    }
	    
	    //SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        //String format = date.format(new Date(System.currentTimeMillis()));
        
	    /* String time1 = "2020-12-01 13:01:20:904";
        String time2 = "2020-12-02 06:00:00:000";
        Format f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = (Date) f.parseObject(time1);
		Timestamp ts = new Timestamp(d1.getTime());
		
		Date d2 = (Date) f.parseObject(time2);
		Timestamp ts2 = new Timestamp(d2.getTime());
		
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = date.format(new Date(System.currentTimeMillis()));
        Date d1 = (Date) date.parseObject(format);
        String time1=Util.DateToString(d1);  
        Integer xx =Util.StringToTimestamp(time1);*/
        
        /*RangeQueryBuilder rangequerybuilder = QueryBuilders
                //传入时间，目标格式2020-01-02T03:17:37.638Z
                //.rangeQuery("in_es_time").from(time1)
        		.rangeQuery("in_es_time")
                .from(time1).to(time2);*/
	    Integer inesTime = Integer.valueOf(inEsTime);
        matchQueryBuilder.must(QueryBuilders.rangeQuery("in_es_time").gt(inesTime));
        //matchQueryBuilder.must(QueryBuilders.rangeQuery("in_es_time").lt(L2));
        //matchQueryBuilder.must(rangequerybuilder);
	    sourceBuilder.query(matchQueryBuilder);
 
	    List<BucketOrder> buck1=new ArrayList<BucketOrder>();
	    buck1.add(BucketOrder.aggregation("sale_qty", true));
	    buck1.add(BucketOrder.aggregation("on_hand_qty", true));
	    buck1.add(BucketOrder.aggregation("on_order_qty", true));
	    buck1.add(BucketOrder.aggregation("write_off_qty", true));
	    buck1.add(BucketOrder.aggregation("return_qty", true));
	    buck1.add(BucketOrder.aggregation("adjustment_qty", true));
	    buck1.add(BucketOrder.aggregation("transfer_out_qty", true));
	    buck1.add(BucketOrder.aggregation("transfer_in_qty", true));
	    buck1.add(BucketOrder.aggregation("receive_qty", true));
	    buck1.add(BucketOrder.aggregation("return_corr_qty", true));
	    buck1.add(BucketOrder.aggregation("receive_corr_qty", true));
	    buck1.add(BucketOrder.aggregation("transfer_out_corr_qty", true));
	    buck1.add(BucketOrder.aggregation("transfer_in_corr_qty", true));
	    
	    
	    //加入聚合
        //字段值项分组聚合
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_store_article")
                //.field("store_cd.keyword")
        		.script(new Script("doc['store_cd.keyword'] +'#'+doc['article_id.keyword']"))
                .order(buck1);
        
        //计算每组的平均balance指标
        aggregation.subAggregation(AggregationBuilders.sum("sale_qty")
                .field("sale_qty"));
        aggregation.subAggregation(AggregationBuilders.sum("on_hand_qty")
                .field("on_hand_qty"));
        aggregation.subAggregation(AggregationBuilders.sum("on_order_qty")
                .field("on_order_qty"));
        aggregation.subAggregation(AggregationBuilders.sum("write_off_qty")
                .field("write_off_qty"));
        aggregation.subAggregation(AggregationBuilders.sum("return_qty")
                .field("return_qty"));
        aggregation.subAggregation(AggregationBuilders.sum("adjustment_qty")
                .field("adjustment_qty"));
        aggregation.subAggregation(AggregationBuilders.sum("transfer_out_qty")
                .field("transfer_out_qty"));
        aggregation.subAggregation(AggregationBuilders.sum("transfer_in_qty")
                .field("transfer_in_qty"));
        aggregation.subAggregation(AggregationBuilders.sum("receive_qty")
                .field("receive_qty"));
        aggregation.subAggregation(AggregationBuilders.sum("return_corr_qty")
                .field("return_corr_qty"));
        aggregation.subAggregation(AggregationBuilders.sum("receive_corr_qty")
                .field("receive_corr_qty"));
        aggregation.subAggregation(AggregationBuilders.sum("transfer_out_corr_qty")
                .field("transfer_out_corr_qty"));
        aggregation.subAggregation(AggregationBuilders.sum("transfer_in_corr_qty")
                .field("transfer_in_corr_qty"));
        
        // 加入count查询
        CardinalityAggregationBuilder cardinalityAggregationBuilder = AggregationBuilders.cardinality("by_store_article_count")
                .script(new Script("doc['store_cd.keyword'] +'#'+doc['article_id.keyword']"));
        
        sourceBuilder.aggregation(aggregation);
        sourceBuilder.aggregation(cardinalityAggregationBuilder);
          
        aggregation.size(Integer.MAX_VALUE);
	    	
        searchRequest.source(sourceBuilder);

	    
	    //3、发送请求
	    SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
	    
  
	    //4、处理响应
        //搜索结果状态信息
	    String json="";
        List<Map<String, Object>> result=new ArrayList<>();
        List<Map<String, Object>> resultLast=new ArrayList<>();
        if(RestStatus.OK.equals(searchResponse.status())) {
        	mapLast.put("status", "0");
        	mapLast.put("message", "OK");
            // 获取聚合结果
            Aggregations aggregations = searchResponse.getAggregations();
            Terms byAgeAggregation = aggregations.get("by_store_article");
            Cardinality cardinality = aggregations.get("by_store_article_count");
            long rowCount = cardinality.getValue();;
            int i = 0;
            for(Terms.Bucket buck : byAgeAggregation.getBuckets()) {
                // 手动分页
                if (page!=null && !page.equals("")&& !page.equals("*") && size!=null && !size.equals("")&& !size.equals("*")) {
                    if(i++<(Integer.valueOf(page)-1) * Integer.valueOf(size)){
                        continue;
                    }
                    if (i > Integer.valueOf(page) * Integer.valueOf(size)) {
                        break;
                    }
                }
                Map<String, Object> map=new HashMap<String, Object>();
                String[] arr= buck.getKeyAsString().split("#");
                map.put("store_cd",arr[0].replace("[","").replace("]",""));
                map.put("article_id",arr[1].replace("[","").replace("]",""));
                //取子聚合
                //ParsedValueCount averageBalance = buck.getAggregations().get("sum");
                Sum sum_sale_qty=buck.getAggregations().get("sale_qty");
                Sum sum_on_hand_qty=buck.getAggregations().get("on_hand_qty");
                Sum sum_on_order_qty=buck.getAggregations().get("on_order_qty");
                Sum sum_write_off_qty=buck.getAggregations().get("write_off_qty");
                Sum sum_return_qty=buck.getAggregations().get("return_qty");
                Sum sum_adjustment_qty=buck.getAggregations().get("adjustment_qty");
                Sum sum_transfer_out_qty=buck.getAggregations().get("transfer_out_qty");
                Sum sum_transfer_in_qty=buck.getAggregations().get("transfer_in_qty");
                Sum sum_return_corr_qty=buck.getAggregations().get("return_corr_qty");
                Sum sum_receive_qty=buck.getAggregations().get("receive_qty");
                Sum sum_receive_corr_qty=buck.getAggregations().get("receive_corr_qty");
                Sum sum_transfer_out_corr_qty=buck.getAggregations().get("transfer_out_corr_qty");
                Sum sum_transfer_in_corr_qty=buck.getAggregations().get("transfer_in_corr_qty");
                map.put("sale_qty",new BigDecimal(sum_sale_qty.getValue()).floatValue());
                map.put("on_hand_qty",new BigDecimal(sum_on_hand_qty.getValue()).floatValue());
                map.put("on_order_qty",new BigDecimal(sum_on_order_qty.getValue()).floatValue());
                map.put("write_off_qty",new BigDecimal(sum_write_off_qty.getValue()).floatValue());
                map.put("return_qty",new BigDecimal(sum_return_qty.getValue()).floatValue());
                map.put("adjustment_qty",new BigDecimal(sum_adjustment_qty.getValue()).floatValue());
                map.put("transfer_out_qty",new BigDecimal(sum_transfer_out_qty.getValue()).floatValue());
                map.put("transfer_in_qty",new BigDecimal(sum_transfer_in_qty.getValue()).floatValue());
                map.put("return_corr_qty",new BigDecimal(sum_return_corr_qty.getValue()).floatValue());
                map.put("receive_qty",new BigDecimal(sum_receive_qty.getValue()).floatValue());
                map.put("receive_corr_qty",new BigDecimal(sum_receive_corr_qty.getValue()).floatValue());
                map.put("transfer_out_corr_qty",new BigDecimal(sum_transfer_out_corr_qty.getValue()).floatValue());
                map.put("transfer_in_corr_qty",new BigDecimal(sum_transfer_in_corr_qty.getValue()).floatValue());
                result.add(map);
            }
            
            json=JSON.toJSONString(result);
            mapLast.put("content", json);
            mapLast.put("size", rowCount);
        }
        else {
        	mapLast.put("status", "1");
        	mapLast.put("message", "NG");
        	mapLast.put("content", "");
        }

        resultLast.add(mapLast);
        String jsonLast=JSON.toJSONString(resultLast);
        client.close();
	    
		return  jsonLast;
	}

    @Override
    public String SaveRelTimeInventory(String realTimeJson) throws IOException,ParseException {
        String host = Util.getProperty("es.nodes");
        int port = Integer.valueOf(Util.getProperty("es.port"));
        String schema = "http";
        RestHighLevelClient client= esUtil.getHighLevelClient(host, port, schema);
        
        //搜索结果状态信息
        Map<String, String> mapLast=new HashMap<String, String>();
        List<RealTimeDto> realTimeDtos;
        try {
            realTimeDtos = JSON.parseArray(realTimeJson, RealTimeDto.class);
        } catch (Exception e) {
            mapLast.put("status", "4");
            mapLast.put("message", "The Json Data Error!");
            client.close();
            String jsonLast=JSON.toJSONString(mapLast);
            return jsonLast;
        }
        BulkRequest bulkRequest = new BulkRequest();
        for (RealTimeDto realTimeDto : realTimeDtos) {
            if (Util.IsBlank(realTimeDto.getStoreCd())) {
                mapLast.put("status", "1");
                mapLast.put("message", "The Store Code can not be empty!");
                client.close();
                String jsonLast=JSON.toJSONString(mapLast);
                return jsonLast;
            }
            if (Util.IsBlank(realTimeDto.getArticleId())) {
                mapLast.put("status", "2");
                mapLast.put("message", "The Item Code can not be empty!");
                client.close();
                String jsonLast=JSON.toJSONString(mapLast);
                return jsonLast;
            }
            Map<String, Object> jsonMap = Util.dtoToMap(realTimeDto);
            // 添加数据
            //准备json数据
            IndexRequest request = new IndexRequest("tran_detail").source(jsonMap);
            
            bulkRequest.add(request);
        }
        
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        
        if(RestStatus.OK.equals(bulkResponse.status())) {
            // 返回结果
            mapLast.put("status", "0");
            mapLast.put("message", "Data Saved Successfully!");
            System.out.println("Data Saved Successfully!");
            client.close();
        }else {
            // 返回结果
            mapLast.put("status", "3");
            mapLast.put("message", "Data Saving Failed!");
            System.out.println("Data Saving Failed!");
            client.close();
        }
        
        
        String jsonLast=JSON.toJSONString(mapLast);
        return jsonLast;
    }

    @Override
    public void SaveRelTimeInventoryByFaildata() throws IOException, ParseException, SQLException {
        String host = Util.getProperty("es.nodes");
        int port = Integer.valueOf(Util.getProperty("es.port"));
        String schema = "http";
        RestHighLevelClient client= esUtil.getHighLevelClient(host, port, schema);

        List<RealTimeDto> dtos = getSaleDetailList();
        if(dtos.isEmpty()||dtos==null){
        }else{
            for(RealTimeDto dto:dtos){
                //搜索结果状态信息
                Map<String, String> mapLast=new HashMap<String, String>();
                if (Util.IsBlank(dto.getStoreCd())) {
                    System.out.println("this store is null");
                    break;
                }
                if (Util.IsBlank(dto.getArticleId())) {
                    System.out.println("this Item Code is null");
                    break;
                }
                // 添加数据
                //准备json数据
                Map<String, Object> jsonMap = Util.dtoToMap(dto);
                IndexRequest request = new IndexRequest("tran_detail").source(jsonMap);

                IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);

                if(RestStatus.CREATED.equals(indexResponse.status())) {
                    delSaleDetail(dto);
                }else {
                    // 返回结果
                    System.out.println("Data Saving Failed!");
                    break;
                }
            }
            client.close();
        }
    }

    public static List<RealTimeDto> getSaleDetailList() {
        String sql = "select store_cd as \"storeCd\"," +
                "article_id as \"articleId\"," +
                "detail_type as \"detailType\"," +
                "in_es_time as \"inEsTime\"," +
                "sale_qty as \"saleQty\"," +
                "on_hand_qty as \"onHandQty\"," +
                "on_order_qty as \"onOrderQty\"," +
                "write_off_qty as \"writeOffQty\"," +
                "return_qty as \"returnQty\"," +
                "adjustment_qty as \"adjustmentQty\"," +
                "transfer_out_qty as \"transferOutQty\"," +
                "transfer_in_qty as \"transferInQty\"," +
                "receive_qty as \"receiveQty\"," +
                "receive_corr_qty as \"receiveCorrQty\"," +
                "return_corr_qty as \"returnCorrQty\"," +
                "transfer_out_corr_qty as \"transferOutCorrQty\"," +
                "transfer_in_corr_qty as \"transferInCorrQty\"," +
                "dep_cd as \"depCd\"," +
                "pma_cd as \"pmaCd\"," +
                "category_cd as \"categoryCd\"," +
                "sub_category_cd as \"subCategoryCd\", " +
                "create_ymd_hms as \"createYmdHms\" " +
                "from sk0030 ";

        Util dbOp=new Util();
        return dbOp.getList(sql, RealTimeDto.class);
    }

    public static void delSaleDetail(RealTimeDto dto) throws SQLException {
        String sql = "delete from sk0030 " +
                "where store_cd='"+dto.getStoreCd()+"'"+
                " and article_id='"+dto.getArticleId()+"'"+
                " and detail_type='"+dto.getDetailType()+"'"+
                " and create_ymd_hms='"+dto.getCreateYmdHms()+"'";
        Util dbOp=new Util();
        dbOp.operateDB(sql);
    }
}
