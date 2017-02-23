package util;

import com.sanhai.common.util.CommonUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者：boys
 * 创建时间：2017-02-18 17:28
 * 类描述：
 * 修改人：
 * 修改时间：
 */
public class ElasticSearchServer {

    //es集群IP地址
    private String clusterIp = "192.168.1.215";
    //es数据交换端口
    private int port = 9300;

    private static Logger logger = Logger.getLogger(ElasticSearchServer.class);

    public static ElasticSearchServer getInstance(){
        return ElasticServerHolder.elasticSearchServer;
    }

    private TransportClient client;

    private static class ElasticServerHolder {
        private static final ElasticSearchServer elasticSearchServer = new ElasticSearchServer();
    }

    private ElasticSearchServer(){
        //设置集群名称
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        //创建client
        try {
            this.client = TransportClient.builder().settings(settings).build();
            this.client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(clusterIp), port));
        } catch (UnknownHostException e) {
            logger.error(e, e);
        }
    }

    /**
     * 创建一个索引
     * @param indexName 索引名
     */
    public void createIndex(String indexName) {
        try {
            CreateIndexResponse indexResponse = this.client
                    .admin()
                    .indices()
                    .prepareCreate(indexName)
                    .get();
            System.out.println(indexResponse.isAcknowledged()); // true表示创建成功
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给索引增加mapping。
     * @param index 索引名
     * @param type mapping所对应的type
     */
    public void addMapping(String index, String type, JSONObject mapping) {
        try {
            // 使用XContentBuilder创建Mapping
            XContentBuilder builder =
                    XContentFactory.jsonBuilder()
                            .startObject()
                            .field("properties")
                            .startObject()
                            .field("name")
                            .startObject()
                            .field("index", "not_analyzed")
                            .field("type", "string")
                            .endObject()
                            .field("age")
                            .startObject()
                            .field("index", "not_analyzed")
                            .field("type", "integer")
                            .endObject()
                            .endObject()
                            .endObject();
            System.out.println(builder.string());
            PutMappingRequest mappingRequest = Requests.putMappingRequest(index).source(builder).type(type);
            this.client.admin().indices().putMapping(mappingRequest).actionGet();
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引
     * @param index 要删除的索引名
     */
    public void deleteIndex(String index) {
        DeleteIndexResponse deleteIndexResponse =
                this.client
                        .admin()
                        .indices()
                        .prepareDelete(index)
                        .get();
        System.out.println(deleteIndexResponse.isAcknowledged()); // true表示成功
    }

    /**
     * 创建一个文档
     * @param index index
     * @param type type
     */
    public void createDoc(String index, String type) {

        try {
            // 使用XContentBuilder创建一个doc source
            XContentBuilder builder =
                    XContentFactory.jsonBuilder()
                            .startObject()
                            .field("name", "zhangsan")
                            .field("age", "lisi")
                            .endObject();

            IndexResponse indexResponse = this.client
                    .prepareIndex()
                    .setIndex(index)
                    .setType(type)
                            // .setId(id) // 如果没有设置id，则ES会自动生成一个id
                    .setSource(builder.string())
                    .get();
//            System.out.println(indexResponse.getResult());
        } catch (ElasticsearchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 更新文档
     * @param index
     * @param type
     * @param id
     * @param jsonObj 更新内容
     */
    public void updateDoc(String index, String type, String id, String jsonObj) {
        try {
            UpdateResponse updateResponse =
                    this.client
                            .prepareUpdate()
                            .setIndex(index)
                            .setType(type)
                            .setId(id)
                            .setDoc(jsonObj)
                            .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
            * 删除一条数据
    * @param index
    * @param type
    * @param id
    */
    public void deleteDoc(String index, String type, String id) {
        DeleteResponse deleteResponse  = this.client
                .prepareDelete()
                .setIndex(index)
                .setType(type)
                .setId(id)
                .get();
//        System.out.println(deleteResponse.getResult()); // true表示成功
    }

    /**
     * 清除数据
     * @param index
     * @param type
     */
    public void truncateData(String index, String type){
    }

    /**
     * 查询文档
     * @param index   索引
     * @param type    类型
     * @param searchParams   查询参数
     * @param filterParams   过滤参数
     * @return
     */
    public SearchResponse  searchDocs(String index, String type, QueryBuilder searchParams, QueryBuilder filterParams){
        return this.searchDocs(index, type, searchParams, filterParams, 0, 5);
    }

    /**
     * from size分页查询文档
     * @param index   索引
     * @param type    类型
     * @param searchParams   查询参数
     * @param filterParams   过滤参数
     * @param startPage      开始页
     * @param pageSize       页码
     * @return
     */
    public SearchResponse  searchDocs(String index, String type, QueryBuilder searchParams, QueryBuilder filterParams, int startPage, int pageSize){
        logger.debug("### index-->" + index);
        logger.debug("### type-->" + type);
        logger.debug("### searchParams-->" + searchParams.toString());
        logger.debug("### filterParams-->" + filterParams);
        logger.debug("### startPage-->" + startPage);
        logger.debug("### pageSize-->" + pageSize);
        if (CommonUtils.isBlank(index, type)){
            logger.debug("搜索参数 index -->" + index);
            logger.debug("搜索参数 type -->" + type);
            return null;
        }

        if (searchParams == null) return null;

        SearchRequestBuilder requestBuilder = this.client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(searchParams)                 // Query
                .setFrom(startPage).setSize(pageSize).setExplain(true);
        if (filterParams != null){
            requestBuilder.setPostFilter(filterParams);  // Filter
        }

        SearchResponse response = requestBuilder.get();
        logger.info("本次搜索index【" + index + "】下的type【 " + type + "】共命中【" +
                response.getHits().getTotalHits() + "】个doc总耗时【" + response.getTookInMillis() + "】ms");
        return response;
    }


    /**
     * 使用scroll-scan 备份数据
     * @param srcIndex
     * @param type
     * @param destIndex
     * @param pageSize
     * @return
     */
    public void backupData(String srcIndex, String destIndex, String type, int pageSize){
        logger.debug("### srcIndex-->" + srcIndex);
        logger.debug("### type-->" + type);
        logger.debug("### destIndex-->" + destIndex);
        logger.debug("### pageSize-->" + pageSize);
        if (CommonUtils.isBlank(srcIndex,destIndex,type)){
            logger.info("### 参数校验失败，未备份数据");
            return;
        }
        if (pageSize == 0) pageSize = 1000;
        QueryBuilder searchParams = QueryBuilders.matchAllQuery();
        /**
         * 首次查询获取scroll_id
         */
        SearchResponse scrollResp = this.client.prepareSearch(srcIndex)
                .setTypes(type)
                .setSearchType(SearchType.SCAN)
                .setScroll(new TimeValue(1000*60))
                .setQuery(searchParams)
                .setSize(pageSize).execute().actionGet();
        List<String> builder = new ArrayList<String>();
        while(true){
            scrollResp = this.client.prepareSearchScroll(scrollResp.getScrollId())
                    .setScroll(new TimeValue(1000*60))
                    .execute().actionGet();
            if (scrollResp.getHits().getHits().length == 0) break;
            SearchHit[] hits = scrollResp.getHits().getHits();
            for (SearchHit hit : hits){
                Map<String, Object> topic = hit.getSource();
                builder.add(JSONObject.fromObject(topic).toString());
            }
            this.importDataInBatch(destIndex, type, builder);
            builder.clear();
        }
    }

    /**
     * 批量导入数据
     * @param index  库
     * @param type   表
     * @param jsonData  json数据
     */
    public void importDataInBatch(String index, String type, String jsonData){
        BulkRequestBuilder bulkRequest= this.client.prepareBulk();
        bulkRequest.add(this.client.prepareIndex(index, type).setSource(jsonData));
        try {
            BulkResponse response = bulkRequest.execute().actionGet();

            logger.info("### 批量导入index【" + index + "】下的type【" + type + "】的doc【"+response.getItems().length+"】,共耗时【" + response.getTookInMillis() + "】ms");
        }catch (Exception e){
            logger.error(e,e );
        }
    }

    /**
     * 批量导入数据
     * @param index  库
     * @param type   表
     * @param jsonList  json数据
     */
    public void importDataInBatch(String index, String type, List<String> jsonList){
        BulkRequestBuilder bulkRequest= this.client.prepareBulk();
        if (jsonList == null || jsonList.size() == 0) return;
        for (int start = 0; start < jsonList.size(); start++) {
            bulkRequest.add(this.client.prepareIndex(index, type).setSource(jsonList.get(start)));
        }
        try {
            BulkResponse response = bulkRequest.execute().actionGet();
            logger.info("### 批量导入index【" + index + "】下的type【" + type + "】的doc【"+response.getItems().length+"】,共耗时【" + response.getTookInMillis() + "】ms");
        }catch (Exception e){
            logger.error(e,e );
        }
    }

    public TransportClient getClient(){
        return this.client;
    }
}
