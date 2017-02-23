import com.sanhai.common.util.CommonUtils;
import com.sanhai.entity.ESTopic;
import com.sanhai.entity.ESTopicSearch;
import com.sanhai.repository.BHTopicDao;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import util.ContantUtil;
import util.ElasticSearchServer;
import util.HtmlUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：tianjiayuan
 * 创建时间：2017-02-21 11:31
 * 类描述：
 * 修改人：
 * 修改时间：
 */
@ContextConfiguration(locations = {
        "classpath:/conf/spring-config.xml",
})
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportDataTest {

    private static String[] bArry = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j",  "k", "l" , "m", "n"};

    @Resource
    private BHTopicDao bhTopicDao;

    @Test
    public void testImportTopicData(){
        ElasticSearchServer elasticSearchServer = ElasticSearchServer.getInstance();
        int startPage = 0;
        int pageSize = 1000;
        while(true) {
            String sqlLimt = "limit "+ startPage + "," +  pageSize;
            List<Map<String, Object>> list = this.bhTopicDao.loadTopics(sqlLimt);
            if (list == null || list.size() == 0) break;
            startPage += pageSize;
            StringBuilder jsonBuilder = new StringBuilder();
            int count = 0;
            List<String> jsonList = new ArrayList<>();
            for (Map<String, Object> map : list) {
                String content = (String) map.get("content");
                if (CommonUtils.isBlank(content)){
                    continue;
                }
                String topicId = String.valueOf((Long) map.get("topicId"));
                String gradeId = String.valueOf((Integer) map.get("gradeId"));
                String subjectId = String.valueOf((Integer) map.get("subjectId"));
                String jsonAnswer = (String) map.get("jsonAnswer");
                String kId = (String) map.get("kId");
                ESTopic topic = new ESTopic();
                topic.setGradeId(gradeId);
                topic.setGrade(ContantUtil.gradeId2Grade(gradeId));
                topic.setTopicId(topicId);
                topic.setSubjectId(subjectId);
                topic.setSubject(ContantUtil.subjectId2Subject(subjectId));
                topic.setKnowledgePoint(kId);
                StringBuilder contentHtml = new StringBuilder();
                StringBuilder answerBuilder = new StringBuilder();
                contentHtml.append(content);
                if (!CommonUtils.isBlank(jsonAnswer)) {
                    JSONObject anwserJson = JSONObject.fromObject(jsonAnswer);
                    if (anwserJson.containsKey("left")) {
                        JSONArray jsonArray = anwserJson.getJSONArray("left");
                        if (jsonArray != null && jsonArray.size() > 0) {
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject answer = jsonArray.getJSONObject(i);
                                if (answer.containsKey("c")) {
                                    try {
                                        answerBuilder.append(bArry[i] + ".");
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    answerBuilder.append(answer.get("c"));
                                }
                            }
                        }
                    }
                }
                contentHtml.append(answerBuilder.toString());
                topic.setContentHtml(contentHtml.toString());
                topic.setContentText(HtmlUtil.getTextFromHtml(contentHtml.toString()));
                jsonList.add(JSONObject.fromObject(topic).toString());
            }
            elasticSearchServer.importDataInBatch("business_data_v2", "topic", jsonList);
        }
    }

    /**
     * 导入业务数据
     */
    @Test
    public void testImportTopicData2(){
        ElasticSearchServer elasticSearchServer = ElasticSearchServer.getInstance();
        QueryBuilder searchParams = QueryBuilders.matchAllQuery();

        int start = 0;
        int pageSize = 2000;
        while (true){
            List<String> builder = new ArrayList<String>();
            SearchResponse resp = elasticSearchServer.searchDocs("business_data", "video", searchParams, null, start, pageSize);
            if (resp == null) break;
            if (resp.getHits().getTotalHits() == 0) break;
            SearchHit[] hits = resp.getHits().getHits();
            for (SearchHit hit : hits){
                Map<String, Object> topic = hit.getSource();
                String expoundTopics = (String) topic.get("expoundtopics");
                String sameTopics = (String) topic.get("sametopics");
                String topics = "";
                if (!CommonUtils.isBlank(expoundTopics)){
                    topics += expoundTopics.replace(",", " ");
                }
                if (!CommonUtils.isBlank(sameTopics)){
                    topics += sameTopics.replace(",", " ");
                }
                if (!CommonUtils.isBlank(topics)) {
                    QueryBuilder query = QueryBuilders.termsQuery("topicId", topics);
                    SearchResponse topicResp = elasticSearchServer.searchDocs("business_data", "topic", query, null, 0, 1000);
                    SearchHit[] topicHits = topicResp.getHits().getHits();
                    List<Map<String, Object>> topicList = new ArrayList<>();
                    for (SearchHit hit1 : topicHits) {
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("contenthtml", hit1.getSource().get("contextHtml"));
                        map1.put("contenttext", hit1.getSource().get("contextText"));
                        map1.put("topicId", hit1.getSource().get("topicId"));
                        topicList.add(map1);
                    }
                    topic.put("topicinfo", topicList);
                }
                builder.add(JSONObject.fromObject(topic).toString());
            }
            elasticSearchServer.importDataInBatch("business_data_v2", "video", builder);
            start += pageSize;
        }
    }

    @Test
    public void testUpdateData(){
        ElasticSearchServer elasticSearchServer = ElasticSearchServer.getInstance();
//        elasticSearchServer.updateDoc("business_data_v2", "video", );
    }

    @Test
    public void importDataByScroll(){
        ElasticSearchServer elasticSearchServer = ElasticSearchServer.getInstance();
        TransportClient client = elasticSearchServer.getClient();
        QueryBuilder searchParams = QueryBuilders.matchAllQuery();
        int pageSize = 1000;
        SearchResponse scrollResp =client.prepareSearch("business_data")
                .setTypes("video")
                .setSearchType(SearchType.SCAN)
                .setScroll(new TimeValue(1000*60))
                .setQuery(searchParams)
                .setSize(pageSize).execute().actionGet();

        while(true){
            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId())
                    .setScroll(new TimeValue(1000*60))
                    .execute().actionGet();
            if (scrollResp.getHits().getHits().length == 0) break;
            List<String> builder = new ArrayList<String>();
            SearchHit[] hits = scrollResp.getHits().getHits();
            for (SearchHit hit : hits){
                Map<String, Object> topic = hit.getSource();
                String expoundTopics = (String) topic.get("expoundtopics");
                String sameTopics = (String) topic.get("sametopics");
                String topics = "";
                if (!CommonUtils.isBlank(expoundTopics)){
                    topics += expoundTopics.replace(",", " ");
                }
                if (!CommonUtils.isBlank(sameTopics)){
                    topics += sameTopics.replace(",", " ");
                }
                if (!CommonUtils.isBlank(topics)) {
                    QueryBuilder query = QueryBuilders.matchQuery("topicId", topics);
                    SearchResponse topicResp = elasticSearchServer.searchDocs("business_data", "topic", query, null, 0, 1000);
                    SearchHit[] topicHits = topicResp.getHits().getHits();
                    if (topicHits.length == 0) continue;
                    List<Map<String, Object>> topicList = new ArrayList<>();
                    for (SearchHit hit1 : topicHits) {
                        Map<String, Object> map1 = new HashMap<>();
                        Map<String, Object> map2 = hit1.getSource();
                        map1.put("contenthtml", map2.get("contexthtml"));
                        map1.put("contenttext", map2.get("contexttext"));
                        map1.put("topicId", map2.get("topicId"));
                        topicList.add(map1);
                    }
                    topic.put("topicinfo", topicList);
                    try {
                        XContentBuilder doc = XContentFactory.jsonBuilder();
                        doc.startObject();
                        for (Map.Entry<String, Object> entry : topic.entrySet()){
//                            if (entry.getKey().equalsIgnoreCase("topicinfo")){
//                                doc.array(entry.getKey(), entry.getValue());
//                            }else {
                                doc.field(entry.getKey(), entry.getValue());
//                            }
                        }
                        doc.endObject();
                        System.out.println(doc.string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    topic.put("topicinfo", new ArrayList<>());
                }


                builder.add(JSONObject.fromObject(topic).toString());
            }
//            elasticSearchServer.importDataInBatch("business_data_v2", "video", builder);
        }

    }

    @Test
    public void testSearch(){
        ElasticSearchServer elasticSearchServer = ElasticSearchServer.getInstance();
        String topics = "80855432 808109289  808189426  80855432  808109681  808234070  808247207  808162722  808162618  808168205  808245871 ";
        QueryBuilder query = QueryBuilders.termQuery("topicId", topics);
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("topicId", topics);
        SearchResponse topicResp = elasticSearchServer.searchDocs("business_data", "topic", queryBuilder, null, 0, 1000);
        System.out.println(topicResp.getHits().getHits().length);

    }

}
