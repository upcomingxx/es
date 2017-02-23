import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import util.ElasticSearchServer;

import java.util.Map;

/**
 * 作者：tianjiayuan
 * 创建时间：2017-02-18 13:44
 * 类描述：
 * 修改人：
 * 修改时间：
 */
public class ElasticSearchTest {

    public static void main(String[] args) {
        ElasticSearchServer elasticSearchServer = ElasticSearchServer.getInstance();

        QueryBuilder queryBuilder = QueryBuilders.termQuery("content", "勾股定理");
//        queryBuilder.boost(2);
        String str = "5依依住在玎层每层楼梯的台也I  一  数都是相同的，每两层之间有      组楼梯，每组有8级台阶。依依      从1层到17层，她一共走了多少    级台阶?    ";
        MatchQueryBuilder queryBuilder1 = QueryBuilders.matchQuery("content", str);
        queryBuilder1.minimumShouldMatch("50%");

        SearchResponse resp = elasticSearchServer.searchDocs("nep_service", "topic", queryBuilder1, null);
        SearchHits searchHits = resp.getHits();
        System.out.println(searchHits.getTotalHits());
        System.out.println(searchHits.totalHits());
        System.out.println(searchHits);
        for (SearchHit searchHit : searchHits){
            Map<String,Object> map = searchHit.getSource();
            System.out.println("--------------------------------------");
            for (Map.Entry entry : map.entrySet()){
                System.out.println(entry.getKey() + ":" + entry.getValue());
            }
            System.out.println("+++++++++++++++++++++++++++++++++++++++");
        }
//        System.out.println(resp);

        //更新
//        elasticSearchServer.updateDoc("video", "video", "AVpPj7voSyDELvBpA0NQ");
    }

}
